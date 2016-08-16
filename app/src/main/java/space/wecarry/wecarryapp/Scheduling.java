package space.wecarry.wecarryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;

import space.wecarry.wecarryapp.item.BusyTimeItem;
import space.wecarry.wecarryapp.item.FreeTimeItem;
import space.wecarry.wecarryapp.item.TaskItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

import static android.provider.BaseColumns._ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.SCHEDULE_EVENT_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_SCHEDULE_LIST;

/**
 * Created by Blair on 2016/8/16.
 */
public class Scheduling {

    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE,          // 2
            CalendarContract.Instances.END             // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;
    private static final int PROJECTION_END_INDEX = 3;
    private Uri uri;

    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private Cursor cursor = null;
    private ArrayList<TaskItem> tasksList;
    private ArrayList<BusyTimeItem> busyTimeItemsList;
    private ArrayList<FreeTimeItem> freeTimeItemsList;
    private ArrayList<TaskItem> overDeadlineList;
    private long nowTime = Calendar.getInstance().getTimeInMillis();
    private long projectDeadline;
    private Context context;

    public Scheduling(Context c, ArrayList<TaskItem> ls) {
        this.context = c;
        busyTimeItemsList = new ArrayList<BusyTimeItem>();
        freeTimeItemsList = new ArrayList<FreeTimeItem>();
        overDeadlineList = new ArrayList<>();
        tasksList = new ArrayList<>();

        // Open db
        dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();

        // Clean events user have inserted
        deleteAllEvent();

        // Clean task which is over deadline
        for(TaskItem ti : ls) {
            long deadline = ti.getDeadline();
            if (deadline <= nowTime) {
                overDeadlineList.add(ti);
            }else {
                tasksList.add(ti);
            }
        }

        // Find the project deadline(the longest one of tasks)
        // Sort by deadline
        Collections.sort(tasksList, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem lhs, TaskItem rhs) {if (lhs.getDeadline() > rhs.getDeadline()) {return 1;} else {return -1;}}});
        projectDeadline = tasksList.get(tasksList.size() -1).getDeadline();

        // Sort by importance and urgency
        Collections.sort(tasksList, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem lhs, TaskItem rhs) {if (lhs.isUrgency() == true && rhs.isUrgency() != true) {return -1;} else {return 1;}}});
        Collections.sort(tasksList, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem lhs, TaskItem rhs) {if (lhs.isImportant() == true && rhs.isImportant() != true) {return -11;} else {return 1;}}});

        // Find busy time from user's calendar
        Cursor cur = getCalendarInstance();
        findBusyTime(cur);

        // Busy time user set
        insertPreferenceBusyTime();

        // Find free time
        findFreeTime();

        // Start scheduling
        scheduleAdapter();

        // Check if some tasks have no time to do
        if(overDeadlineList.size() > 0) {
            Toast.makeText(context, "部分任務超過Deadline無法排程", Toast.LENGTH_SHORT).show();   // TODO: English and better method to show message
        }else {
            Toast.makeText(context, "成功排程", Toast.LENGTH_SHORT).show();
        }

    }

    // To find the events in user's calendar and add in an arrayList (BusyTime)
    private Cursor getCalendarInstance() {
        busyTimeItemsList.clear();
        freeTimeItemsList.clear();
        long startMillis = nowTime;
        long endMillis = projectDeadline;

        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();

        // The ID of the recurring event whose instances you are searching
        // for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " > ?";
        String[] selectionArgs = new String[] {"0"};    // We choose all the events of calendars

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        // Submit the query
        cur =  cr.query(builder.build(),
                INSTANCE_PROJECTION,
                selection,
                selectionArgs,
                null);
        return  cur;
    }

    private void findBusyTime(Cursor cur) {
        while (cur.moveToNext()) {
            long beginVal = 0;
            long endVal = 0;

            // Get the field values
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            endVal = cur.getLong(PROJECTION_END_INDEX);

            // This is the busyTime (the event in user's calendar)
            busyTimeItemsList.add(new BusyTimeItem(beginVal, endVal));
        }
    }

    // To insert sleepTime in arrayList as busyTime
    private void insertPreferenceBusyTime( //, int busyStartHour, int busyEndHour, int busyStartMin, int busyEndMin // TODO: User input
    ) {
        Calendar startSleep = Calendar.getInstance();
        Calendar endSleep = Calendar.getInstance();
        Calendar startLunch = Calendar.getInstance();
        Calendar endLunch = Calendar.getInstance();
        // set sleep time
        startSleep.set(Calendar.HOUR_OF_DAY, 22);
        startSleep.set(Calendar.MINUTE, 0);
        startSleep.add(Calendar.DAY_OF_MONTH, -1);  //we started sleeping yesterday
        endSleep.set(Calendar.HOUR_OF_DAY, 9);
        endSleep.set(Calendar.MINUTE, 0);
        // lunch time
        startLunch.set(Calendar.HOUR_OF_DAY, 12);
        startLunch.set(Calendar.MINUTE,0);
        endLunch.set(Calendar.HOUR_OF_DAY, 13);
        endLunch.set(Calendar.MINUTE, 0);
        // How many days we have to sleep before the (project) deadline
        long day =(projectDeadline - startSleep.getTimeInMillis()) /(24*60*60*1000);
        // add it as a busyTime
        while (day > 0) {
            busyTimeItemsList.add(new BusyTimeItem(startSleep.getTimeInMillis(), endSleep.getTimeInMillis()));
            busyTimeItemsList.add(new BusyTimeItem(startLunch.getTimeInMillis(), endLunch.getTimeInMillis()));
            startSleep.add(Calendar.DAY_OF_MONTH, 1);
            endSleep.add(Calendar.DAY_OF_MONTH, 1);
            startLunch.add(Calendar.DAY_OF_MONTH,1);
            endLunch.add(Calendar.DAY_OF_MONTH, 1);
            day--;
        }
    }

    // We find the freeTime between busyTime and busyTime
    private void findFreeTime() {
        // Sort the busyTime by startTime (/deadline)
        Collections.sort(busyTimeItemsList, new Comparator<BusyTimeItem>() {
            @Override
            public int compare(BusyTimeItem lhs, BusyTimeItem rhs) {
                if (lhs.getStart() > rhs.getStart()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        // freeTime = busyTime - busyTime
        long startWorking = nowTime + 15*60*1000;    // buffer 15 minutes before starting scheduling
        long freeStart = 0;
        long freeEnd = 0;
        for (BusyTimeItem busy : busyTimeItemsList) {
            if (startWorking < busy.getStart()) {    // startWorking is before busyStart
                freeStart = startWorking;
                freeEnd = busy.getStart();
                if(freeStart < (freeEnd - 15*60*1000)) {         // the free time must be more than 15 minute
                    freeTimeItemsList.add(new FreeTimeItem(freeStart, freeEnd));
                }
                startWorking = busy.getEnd();
            } else {
                if(startWorking < busy.getEnd()) {
                    startWorking = busy.getEnd();
                }
            }
        }
    }

    // All about scheduling is in here
    // Currently we just insert a task in a freeTime.
    private void scheduleAdapter() {
        int indexFreeTime = 0;
        long tolerance = 59999;
        for(TaskItem ti : tasksList) {
            long duration = ti.getDuration() ;
            int insertTimes = 0;    // Number of times of inserted to calendar
            // Give a free time to a task,
            // so all of duration of a task should be used with free time
            while(duration > (0 + tolerance)) {
                // if the duration has not been consumed
                // we give it free time
                if(indexFreeTime < freeTimeItemsList.size()) {
                    duration-= (freeTimeItemsList.get(indexFreeTime).getAvailableTime());
                    long startTime =freeTimeItemsList.get(indexFreeTime).getStart();
                    long endTime = freeTimeItemsList.get(indexFreeTime).getEnd();
                    if(duration <0) {
                        endTime = (freeTimeItemsList.get(indexFreeTime).getAvailableTime()) + duration + startTime;
                        // Do not waste the remaining time
                        freeTimeItemsList.add((indexFreeTime +1), new FreeTimeItem(endTime, freeTimeItemsList.get(indexFreeTime).getEnd()));
                    }
                    if(ti.getDeadline() < startTime) {
                        // task is over deadline
                        overDeadlineList.add(ti);
                        break;
                    }
                    addEventToCalendar(ti.getTitle() + "(" + Integer.toString(insertTimes + 1) + ")", startTime, endTime);
                    insertTimes++;
                    indexFreeTime++;
                }else {
                    //have no time to do the tasks
                    overDeadlineList.add(ti);
                    break;
                }
            }
        }
    }

    private void addEventToCalendar(String title, long startMillis, long endMillis) {
        long calID = 1; // The id of calendar we insert event
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(TimeZone.getAvailableIDs()));
        uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        saveEventUriToDB(uri.toString());
    }

    private void deleteAllEvent() {
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_SCHEDULE_LIST, null );
        int dataRow = cursor.getCount();
        if(dataRow > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < dataRow; i++) {
                String uriString = cursor.getString(2);
                deleteEvent(uriString);
                cursor.moveToNext();
            }
        }
        db.delete(TABLE_NAME_SCHEDULE_LIST, _ID + " > " + "-1", null);  // clean the data from db
    }

    private void saveEventUriToDB(String uri) {
        // Open db
        dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SCHEDULE_EVENT_ID, uri);
        db.insert(TABLE_NAME_SCHEDULE_LIST, null, cv);
    }

    private void deleteEvent(String uri) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        deleteUri = Uri.parse(uri);
        int rows = context.getContentResolver().delete(deleteUri, null, null);
        if(rows >1) {
            Toast.makeText(context, "Something wrong!!", Toast.LENGTH_SHORT).show();
        }
    }
}


