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
                        TASK_EST + " INTEGER, " +
                        TASK_LST + " INTEGER, " +
                        TASK_EET + " INTEGER, " +
                        TASK_LET + " INTEGER, " +
                        TASK_DEADLINE + "INTEGER, " +
                        TASK_DURATION + " INTEGER, " +
                        TASK_PREPROCESS + " TEXT, " +
                        TASK_RESOURCE + " TEXT, " +
                        TASK_GOAL_ID + " INTEGER " +
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
                        TASK_GOAL_ID + " INTEGER " +
                        ");";

        db.execSQL(INIT_ROLE_TABLE);
        db.execSQL(INIT_GOAL_TABLE);
        db.execSQL(INIT_TASK_TABLE);

        db.execSQL((INIT_TEST_GOAL_TABLE));
        db.execSQL((INIT_TEST_TASK_TABLE));

    }


    public void insertGoalWithTasks(GoalItem gi, int goalID, int goalRoleID) {
        Log.d("inserting goal", gi.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();
        // insert all the tasks into task table first.
        for (TaskItem ti : gi.getTaskList()) {
            insertTask(ti, goalID);
        }

        ContentValues cv = new ContentValues();
        cv.put(GOAL_ID, goalID);
        cv.put(GOAL_TITLE, gi.getTitle());
        cv.put(GOAL_DEADLINE, gi.getDeadline());
        cv.put(GOAL_DURATION, gi.getDuration());
        cv.put(GOAL_IMPORTANCE, String.valueOf(gi.isImportant()));
        cv.put(GOAL_URGENCY, String.valueOf(gi.isUrgent()));
        cv.put(GOAL_ROLE_ID, goalRoleID);
        db.insert(TEST_GOAL_TABLE, null, cv);
    }

    public void insertTask(TaskItem ti, int taskGoalID) {
        Log.d("insertTask", ti.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TASK_ID, -1); // Fixme: unknown purpose field.
        cv.put(TASK_TITLE, ti.getTitle());
        cv.put(TASK_EST, 0); // Fixme: just a place holder value
        cv.put(TASK_LST, 0);
        cv.put(TASK_EET, 0);
        cv.put(TASK_LET, 0);
        cv.put(TASK_DEADLINE, ti.getDeadline());
        cv.put(TASK_DURATION, ti.getDuration());
        cv.put(TASK_PREPROCESS, ""); // TODO: think of a way to save this data.
        cv.put(TASK_RESOURCE, "");   // TODO: same^.
        cv.put(TASK_GOAL_ID, taskGoalID);

        db.insert(TEST_TASK_TABLE, null, cv);
    }

    // TODO: goal should have some kind of unique identifier, not just title.
    public GoalItem getGoal(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        GoalItem gi = new GoalItem();
        Cursor cursor = db.query(TEST_GOAL_TABLE, new String[] {_ID,
                        GOAL_ID, GOAL_TITLE, GOAL_DEADLINE, GOAL_DURATION,
                GOAL_IMPORTANCE, GOAL_URGENCY, GOAL_ROLE_ID}, GOAL_TITLE + "=?",
                new String[] { title }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            gi = new GoalItem(
                    cursor.getString(2),
                    cursor.getLong(3),
                    Boolean.parseBoolean(cursor.getString(5)),
                    Boolean.parseBoolean(cursor.getString(6)),
                    new ArrayList<TaskItem>()); // TODO: get task data.
//                    cursor.getString(7));
        }
        return gi;
    }

    public TaskItem getTask(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        TaskItem ti = new TaskItem();
        Cursor cursor = db.query(TEST_TASK_TABLE, new String[] {_ID,
                        TASK_ID, TASK_TITLE, TASK_EST, TASK_LST, TASK_EET, TASK_LET,
                        TASK_DEADLINE, TASK_DURATION, TASK_PREPROCESS, TASK_RESOURCE,
                        TASK_GOAL_ID}, TASK_TITLE + "=?",
                new String[] { title }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            ti = new TaskItem(
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getLong(4),
                    cursor.getLong(5),
                    cursor.getLong(6),
                    cursor.getLong(7),
                    cursor.getLong(8),
                    new ArrayList<TaskItem>(),      // TODO: parse data
                    new ArrayList<ResourceItem>()); // TODO: parse data
        }
        return ti;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String role = "DROP TABLE IF EXISTS " + TABLE_NAME_ROLE_LIST;
        final String goal = "DROP TABLE IF EXISTS " + TABLE_NAME_GOAL_LIST;
        final String task = "DROP TABLE IF EXISTS " + TABLE_NAME_TASK_LIST;
        // Note: To upgrade table by deleting&creating table is not the best way.
        db.execSQL(role);
        db.execSQL(goal);
        db.execSQL(task);
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
