package space.wecarry.wecarryapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.TaskItem;

import static android.provider.BaseColumns._ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.*;

public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "DB";
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
                        GOAL_DURATION + "INTEGER, " +
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
                        GOAL_DURATION + "INTEGER, " +
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
                        TASK_DEADLINE + "INTEGER, " +
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


    public void insertGoal(GoalItem gi, int goalRoleID) {
        Log.d("insertGoal", gi.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(GOAL_ID, -1);
        cv.put(GOAL_TITLE, gi.getTitle());
        cv.put(GOAL_DEADLINE, gi.getDeadline());
        cv.put(GOAL_DURATION, gi.getDuration());
        cv.put(GOAL_IMPORTANCE, Boolean.valueOf(gi.isImportant()));
        cv.put(GOAL_URGENCY, Boolean.valueOf(gi.isUrgent()));
        cv.put(GOAL_ROLE_ID, goalRoleID);
        db.insert(TEST_GOAL_TABLE, null, cv);
    }

//    public void insertTask(TaskItem ti, int tastGoalID) {
//        Log.d("insertTask", ti.toString());
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put(TASK_TITLE, ti.getTitle());
//        cv.put(TASK_EST, 0); // just a place holder value
//        cv.put(TASK_LST, 0);
//        cv.put(TASK_EET, 0);
//        cv.put(TASK_LET, 0);
//        cv.put(TASK_DEADLINE, ti.getDeadline());
//        cv.put(TASK_DURATION, ti.getDuration());
//        cv.put(TASK_PREPROCESS, ti.get);
//        cv.put(TASK_RESOURCE, goalRoleID);
//        db.insert(TEST_GOAL_TABLE, null, cv);
//    }


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
