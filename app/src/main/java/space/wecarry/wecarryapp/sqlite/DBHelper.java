package space.wecarry.wecarryapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.ResourceItem;
import space.wecarry.wecarryapp.item.TaskItem;

import static android.provider.BaseColumns._ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.*;

public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "DB";
    private DBHelper mHelper;
    // for testing
    private static final String TEST_GOAL_TABLE = "test_goal_table";
    private static final String TEST_TASK_TABLE = "test_task_table";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String INIT_ROLE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME_ROLE_LIST +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ROLE_ID + " INTEGER, " +
                        ROLE_TITLE + " TEXT, " +
                        ROLE_DEADLINE + " INTEGER, " +
                        ROLE_DURATION + " INTEGER" +
                        ");";
        final String INIT_GOAL_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME_GOAL_LIST +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GOAL_ID + " INTEGER, " +
                        GOAL_TITLE + " TEXT, " +
                        GOAL_DEADLINE + " INTEGER, " +
                        GOAL_DURATION + " INTEGER, " +
                        GOAL_IMPORTANCE + " TEXT, " +
                        GOAL_URGENCY + " TEXT, " +
                        GOAL_ROLE_ID + " INTEGER " +
                        ");";
        final String INIT_TASK_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME_TASK_LIST +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TASK_ID + " INTEGER, " +
                        TASK_TITLE + " TEXT, " +
                        TASK_MILESTONE + " TEXT, " +
                        TASK_EST + " INTEGER, " +
                        TASK_LST + " INTEGER, " +
                        TASK_EET + " INTEGER, " +
                        TASK_LET + " INTEGER, " +
                        TASK_DEADLINE + " INTEGER, " +
                        TASK_DURATION + " INTEGER, " +
                        TASK_PREPROCESS + " TEXT, " +
                        TASK_RESOURCE + " TEXT, " +
                        TASK_GOAL_ID + " INTEGER, " +
                        TASK_ROLE_ID + " INTEGER, " +
                        TASK_IMPORTANCE + " TEXT, " +
                        TASK_URGENCY + " TEXT " +
                        ");";

        final String INIT_RESOURCE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME_RESOURCE_LIST +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RESOURCE_ID + " INTEGER, " +
                        RESOURCE_TITLE + " TEXT, " +
                        RESOURCE_EMAIL + " TEXT, " +
                        RESOURCE_TASK_ID + " INTEGER, " +
                        RESOURCE_GOAL_ID + " INTEGER, " +
                        RESOURCE_ROLE_ID + " INTEGER " +
                        ");";

        final String INIT_SCHEDULE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME_SCHEDULE_LIST +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SCHEDULE_TASK_ID + " INTEGER, " +
                        SCHEDULE_EVENT_ID + " INTEGER " +
                        ");";

        // for testing
        final String INIT_TEST_GOAL_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TEST_GOAL_TABLE +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GOAL_ID + " INTEGER, " +
                        GOAL_TITLE + " TEXT, " +
                        GOAL_DEADLINE + " INTEGER, " +
                        GOAL_DURATION + " INTEGER, " +
                        GOAL_IMPORTANCE + " TEXT, " +
                        GOAL_URGENCY + " TEXT, " +
                        GOAL_ROLE_ID + " INTEGER " +
                        ");";
        // for testing
        final String INIT_TEST_TASK_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TEST_TASK_TABLE +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TASK_ID + " INTEGER, " +
                        TASK_TITLE + " TEXT, " +
                        TASK_EST + " INTEGER, " +
                        TASK_LST + " INTEGER, " +
                        TASK_EET + " INTEGER, " +
                        TASK_LET + " INTEGER, " +
                        TASK_DEADLINE + " INTEGER, " +
                        TASK_DURATION + " INTEGER, " +
                        TASK_PREPROCESS + " TEXT, " +
                        TASK_RESOURCE + " TEXT, " +
                        TASK_GOAL_ID + " INTEGER, " +
                        TASK_ROLE_ID + " INTEGER, " +
                        TASK_IMPORTANCE + " TEXT, " +
                        TASK_URGENCY + " TEXT " +
                        ");";

        db.execSQL(INIT_ROLE_TABLE);
        db.execSQL(INIT_GOAL_TABLE);
        db.execSQL(INIT_TASK_TABLE);
        db.execSQL(INIT_RESOURCE_TABLE);
        db.execSQL(INIT_SCHEDULE_TABLE);

        db.execSQL((INIT_TEST_GOAL_TABLE));
        db.execSQL((INIT_TEST_TASK_TABLE));
    }


    // fixme: do not use
//    public void insertGoal(GoalItem gi, int goalID, int goalRoleID) {
//        Log.d("inserting goal", gi.toString());
//        SQLiteDatabase db = this.getWritableDatabase();
//        ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();
//
//        // insert all the tasks into task table first.
//        for (TaskItem ti : gi.getTaskList()) {
//            insertTask(ti, goalID);
//        }
//
//        ContentValues cv = new ContentValues();
//        cv.put(GOAL_ID, goalID);
//        cv.put(GOAL_TITLE, gi.getTitle());
//        cv.put(GOAL_DEADLINE, gi.getDeadline());
//        cv.put(GOAL_DURATION, gi.getDuration());
//        cv.put(GOAL_IMPORTANCE, String.valueOf(gi.isImportant()));
//        cv.put(GOAL_URGENCY, String.valueOf(gi.isUrgent()));
//        cv.put(GOAL_ROLE_ID, goalRoleID);
//        db.insert(TEST_GOAL_TABLE, null, cv); // fixme: test table
//    }

    // TODO: not tested
    public void insertTask(TaskItem ti, int goalID, int roleID) {
        Log.d("insertTask", ti.getTitle());
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isMilestone = false;

        if (ti.getDeadline() == 0)
            isMilestone = true;

        ContentValues cv = new ContentValues();
        // note: let sqlite auto insert _ID field.
        cv.put(TASK_TITLE, ti.getTitle());
        cv.put(TASK_EST, ti.getEarliestStartTime());
        cv.put(TASK_LST, ti.getLatestStartTime());
        cv.put(TASK_EET, ti.getEarliestEndTime());
        cv.put(TASK_LET, ti.getEarliestEndTime());
        cv.put(TASK_DEADLINE, ti.getDeadline());
        cv.put(TASK_DURATION, ti.getDuration());
        cv.put(TASK_PREPROCESS, ""); // TODO: think of a way to save this data.
        cv.put(TASK_RESOURCE, ti.resourceToString()); // TODO: toString incomplete.
        cv.put(TASK_GOAL_ID, goalID);
        cv.put(TASK_ROLE_ID, roleID);
        cv.put(TASK_MILESTONE, String.valueOf(isMilestone));

        db.insert(TABLE_NAME_TASK_LIST, null, cv);
    }

    // fixme: do not use
    // TODO: goal should have some kind of unique identifier, not just title.
//    public GoalItem getGoal(String title) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        GoalItem gi = new GoalItem();
//        int goalID = -1;
//
//        Cursor cursor = db.query(TEST_GOAL_TABLE, new String[] {_ID, // fixme: test table
//                        GOAL_ID, GOAL_TITLE, GOAL_DEADLINE, GOAL_DURATION,
//                GOAL_IMPORTANCE, GOAL_URGENCY, GOAL_ROLE_ID}, GOAL_TITLE + "= ?",
//                new String[] { title }, null, null, null, null);
//        // get goal item and its data (without task items).
//        if (cursor != null) {
//            cursor.moveToFirst();
//            gi = new GoalItem(
//                    cursor.getString(2),
//                    cursor.getLong(3),
//                    Boolean.parseBoolean(cursor.getString(5)),
//                    Boolean.parseBoolean(cursor.getString(6)),
//                    new ArrayList<TaskItem>());
//            // get goalID in order to find its task items
//            goalID = cursor.getInt(1);
//        }
//        // getting task items
//        ArrayList<TaskItem> tasks = getTasksByGoalID(goalID);
//        gi.setTaskList(tasks);
//
//        return gi;
//    }

    // TODO: not tested
    public ArrayList<TaskItem> getTasksByGoalID(int goalID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TaskItem> tasks = new ArrayList<TaskItem>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_TASK_LIST, null);

        if (c.getCount() > 0) {
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                // if this current cursor item is belong to this goalID
                if (c.getInt(c.getColumnIndex(TASK_GOAL_ID)) == goalID) {
                    TaskItem ti = parseTask(c);
                    tasks.add(ti);
                }
            }
        }
        return tasks;
    }

    // fixme: do not use
//    public TaskItem getTask(String title) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        TaskItem ti = new TaskItem();
//        Cursor cursor = db.query(TEST_TASK_TABLE, new String[] {_ID, // Fixme: test table
//                        TASK_ID, TASK_TITLE, TASK_EST, TASK_LST, TASK_EET, TASK_LET,
//                        TASK_DEADLINE, TASK_DURATION, TASK_PREPROCESS, TASK_RESOURCE,
//                        TASK_GOAL_ID}, TASK_TITLE + "=?",
//                        new String[] { title }, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            ti = new TaskItem(
//                    cursor.getString(2),
//                    cursor.getLong(3),
//                    cursor.getLong(4),
//                    cursor.getLong(5),
//                    cursor.getLong(6),
//                    cursor.getLong(7),
//                    cursor.getLong(8),
//                    new ArrayList<TaskItem>(),      // TODO: parse data
//                    new ArrayList<ResourceItem>()); // TODO: parse data
//        }
//        return ti;
//    }

    public ArrayList<GoalItem> getAllGoal() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<GoalItem> goalList = new ArrayList<GoalItem>();
        int goalID = -1;
        GoalItem gi = new GoalItem();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_GOAL_LIST, null);

        if (c.getCount() > 0) {
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                gi = parseGoal(c);
                // get goalID in order to find its task items
                goalID = gi.getGoalId();
                // getting task items
                ArrayList<TaskItem> tasks = getTasksByGoalID(goalID);
                gi.setTaskList(tasks);
                // now this gi should be ready to add to goalList
                goalList.add(gi);
            }
        }
        Log.i("getAllGoal", "goalList size: " + Integer.toString(goalList.size()));
        return goalList;
    }

    public ArrayList<TaskItem> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();
        TaskItem ti = new TaskItem();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_TASK_LIST, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                ti = parseTask(cursor);

                // getting ti's resources.
//                ti.setResourcesList(getResByTaskID(ti.getTaskId()));

                // add this task to taskList
                taskList.add(ti);
                Log.d("DB getAllTasks", ti.toString());
            }
        }
        return taskList;
    }

    public ArrayList<ResourceItem> getResByTaskID(int taskID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ResourceItem> resList = new ArrayList<ResourceItem>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_RESOURCE_LIST, null);

        if (c.getCount() > 0) {
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                // if this current cursor item is belong to this taskID.
                if (c.getInt(c.getColumnIndex(RESOURCE_TASK_ID)) == taskID) {
                    ResourceItem ri = parseResource(c);
                    resList.add(ri);
                    Log.d("DB getResByTaskID", ri.toString());
                }
            }
        }
        return resList;
    }

    private TaskItem parseTask(Cursor c) {
        String resStr = c.getString(c.getColumnIndex(TASK_RESOURCE));
        TaskItem ti = new TaskItem(
                c.getInt(c.getColumnIndex(_ID)),
                c.getInt(c.getColumnIndex(TASK_GOAL_ID)),
                c.getInt(c.getColumnIndex(TASK_ROLE_ID)),
                c.getString(c.getColumnIndex(TASK_TITLE)),
                Boolean.parseBoolean(c.getString(c.getColumnIndex(TASK_MILESTONE))),
                c.getLong(c.getColumnIndex(TASK_EST)),
                c.getLong(c.getColumnIndex(TASK_LST)),
                c.getLong(c.getColumnIndex(TASK_EET)),
                c.getLong(c.getColumnIndex(TASK_LET)),
                c.getLong(c.getColumnIndex(TASK_DEADLINE)),
                c.getLong(c.getColumnIndex(TASK_DURATION)),
                new ArrayList<TaskItem>(), // TODO: parse data
                new ArrayList<ResourceItem>()); // still empty
        ti.setResourceFromString(c.getString(c.getColumnIndex(TASK_RESOURCE)));
        return ti;
    }

    private GoalItem parseGoal(Cursor c) {
        GoalItem gi = new GoalItem(
                c.getInt(c.getColumnIndex(_ID)),
                c.getInt(c.getColumnIndex(GOAL_ROLE_ID)),
                c.getString(c.getColumnIndex(GOAL_TITLE)),
                c.getLong(c.getColumnIndex(GOAL_DEADLINE)),
                // let constructor calculate duration.
                Boolean.parseBoolean(c.getString(c.getColumnIndex(GOAL_IMPORTANCE))),
                Boolean.parseBoolean(c.getString(c.getColumnIndex(GOAL_URGENCY))),
                new ArrayList<TaskItem>()); // still empty
        return gi;
    }

    private ResourceItem parseResource(Cursor c) {
        ResourceItem ri = new ResourceItem(
                c.getInt(c.getColumnIndex(_ID)),
                c.getInt(c.getColumnIndex(RESOURCE_TASK_ID)),
                c.getInt(c.getColumnIndex(RESOURCE_GOAL_ID)),
                c.getInt(c.getColumnIndex(RESOURCE_ROLE_ID)),
                c.getString(c.getColumnIndex(RESOURCE_TITLE)),
                c.getString(c.getColumnIndex(RESOURCE_EMAIL)));
        return ri;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String role = "DROP TABLE IF EXISTS " + TABLE_NAME_ROLE_LIST;
        final String goal = "DROP TABLE IF EXISTS " + TABLE_NAME_GOAL_LIST;
        final String task = "DROP TABLE IF EXISTS " + TABLE_NAME_TASK_LIST;
        final String resource = "DROP TABLE IF EXISTS " + TABLE_NAME_RESOURCE_LIST;
        final String schedule = "DROP TABLE IF EXISTS " + TABLE_NAME_SCHEDULE_LIST;
        // Note: To upgrade table by deleting&creating table is not the best way.
        db.execSQL(role);
        db.execSQL(goal);
        db.execSQL(task);
        db.execSQL(resource);
        db.execSQL(schedule);
        this.onCreate(db);
        Log.d("DBHelper", "onUpgrade");
    }

    public void clearTestTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        final String goal = "DROP TABLE IF EXISTS " + "test_goal_table";
        final String task = "DROP TABLE IF EXISTS " + "test_task_table";
        db.execSQL(goal);
        db.execSQL(task);
        this.onCreate(db);
        Log.d("DBHelper", "upgraded test tables");
    }
}
