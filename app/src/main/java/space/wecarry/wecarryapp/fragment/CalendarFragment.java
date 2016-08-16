package space.wecarry.wecarryapp.fragment;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.Scheduling;
import space.wecarry.wecarryapp.item.TaskItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_TASK_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_DEADLINE;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_DURATION;

/**
 * Created by Ivan IF Chen on 8/2/2016.
 */
public class CalendarFragment extends Fragment {

    private ListView listView;
    private Button buttonSchedule, buttonReset;
    private ArrayList<TaskItem> taskList;
    private SimpleAdapter adapter;
    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private Cursor cursorTask= null;
    String roleTile, goalTitle;
    ArrayList<HashMap<String,String>> list = new ArrayList();

    public CalendarFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_calendar));
        listView = (ListView) rootView.findViewById(R.id.listView);
        buttonSchedule = (Button) rootView.findViewById(R.id.buttonScheduling);
        buttonReset = (Button) rootView.findViewById(R.id.buttonReset);

        // get taskList data from db.
        getTaskData();

        // Sort by deadline,urgency and importance
        Collections.sort(taskList, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem lhs, TaskItem rhs) {if (lhs.getDeadline() > rhs.getDeadline()) {return 1;} else {return -1;}}});
        Collections.sort(taskList, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem lhs, TaskItem rhs) {if (lhs.isUrgency() == true && rhs.isUrgency() != true) {return -1;} else {return 1;}}});
        Collections.sort(taskList, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem lhs, TaskItem rhs) {if (lhs.isImportant() == true && rhs.isImportant() != true) {return -11;} else {return 1;}}});

        //把資料加入ArrayList中
        for(TaskItem ti: taskList){
            HashMap<String,String> item = new HashMap<String,String>();
            // 偷懶----------------------------------------------------------------------------------------------
            long duration = ti.getDuration();
            final CharSequence[] items = {"15 分鐘", "30 分鐘", "1 小時", "2 小時", "4 小時", "8 小時"};    // TODO: To use better method to set resource, maybe we use .xml or cursor
            final long[] time = {15, 30 ,60 ,120, 240, 480}; // 這邊先偷懶
            int position =0;
            for(int j =0; j< time.length; j++) {
                if(duration == time[j]*60*1000L) {
                    position = j;
                    break;
                }
            }
            // ----------------------------------------------------------------------------------------------
            item.put( "title", ti.getTitle());
            item.put( "deadlineAndDuration", "截止日: "+millSecToDateConverter(ti.getDeadline()) + " \n為期時間: "+ items[position].toString());
            list.add(item);
        }

        // setting up adapter
        adapter = new SimpleAdapter(getActivity(), list, android.R.layout.simple_list_item_2,
                new String[] { "title", "deadlineAndDuration" },
                new int[] { android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
        View emptyView = rootView.findViewById(R.id.view_empty);
        listView.setEmptyView(emptyView);

        setAction();

        return rootView;
    }

    public void getTaskData() {
        taskList = new ArrayList<>();
        // Open db
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        cursorTask = db.rawQuery("SELECT * FROM " + TABLE_NAME_TASK_LIST +
                " WHERE " + TASK_DEADLINE + " > " + " 0 " +
                " AND " + TASK_DURATION + " > " + " 0 "
                , null);
        int taskDataRow = cursorTask.getCount();
        if(taskDataRow != 0) {
            cursorTask.moveToFirst();
            for(int i=0; i<taskDataRow; i++) {
                TaskItem taskItem = new TaskItem();
                taskItem.setTaskId(cursorTask.getInt(0));   //  TODO: Now our role id is at column 1, but (in local) SQLite we use column 0 to be the key.
                taskItem.setTitle(cursorTask.getString(2));
                taskItem.setMilestone(Boolean.parseBoolean(cursorTask.getString(3)));
                taskItem.setEarliestStartTime(cursorTask.getInt(4));
                taskItem.setLatestStartTime(cursorTask.getInt(5));
                taskItem.setEarliestStartTime(cursorTask.getInt(6));
                taskItem.setEarliestEndTime(cursorTask.getInt(7));
                taskItem.setDeadline(cursorTask.getLong(8));
                taskItem.setDuration(cursorTask.getLong(9));
                taskItem.setGoalId(cursorTask.getInt(12));
                taskItem.setRoleId(cursorTask.getInt(13));
                taskItem.setImportant(Boolean.parseBoolean(cursorTask.getString(14)));
                taskItem.setUrgency(Boolean.parseBoolean(cursorTask.getString(15)));
                // TODO: Get preprocessList
                // TODO: Get resourcesList
                taskList.add(taskItem);
                cursorTask.moveToNext();
            }

            // TODO: Get Role, Goal Title?
//            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_ROLE_LIST +
//                            " WHERE " + "_ID" + " =  " + taskList.get(0).getRoleId()
//                    , null);

        }
    }

    private String millSecToDateConverter(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date);
        return s;
    }

    private void setAction() {
        buttonSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting scheduling
                new Scheduling(getActivity(), taskList).scheduleAdapter();

                // Go to the Calendar in user's device
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, Calendar.getInstance().getTimeInMillis());
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());
                startActivity(intent);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new Scheduling(getActivity()).deleteAllEvent() > 0) {
                    Toast.makeText(getActivity(), "已重設排程", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "尚未排程", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}
