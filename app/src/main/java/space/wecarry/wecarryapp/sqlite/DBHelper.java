package space.wecarry.wecarryapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.*;

public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "DB";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBHelper", "onCreate");

        final String INIT_ROLE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME_ROLE_LIST +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ROLE_ID + " INTEGER, " +
                        ROLE_TITLE + " TEXT " +
                        ROLE_DEADLINE + "INTEGER" +
                        ROLE_DURATION + "INTEGER" +
                        ");";
        final String INIT_GOAL_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME_ROLE_LIST +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GOAL_ID + " INTEGER, " +
                        GOAL_TITLE + " TEXT, " +
                        GOAL_DEADLINE + " INTEGER, " +
                        GOAL_DURATION + "INTEGER" +
                        GOAL_IMPORTANCE + " INTEGER, " +
                        GOAL_URGENCY + " INTEGER, " +
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
                        TASK_DEADLINE + "INTEGER" +
                        TASK_DURATION + " INTEGER, " +
                        TASK_PREPROCESS + " TEXT, " +
                        TASK_RESOURCE + " TEXT, " +
                        TASK_GOAL_ID + " INTEGER " +
                        ");";

        db.execSQL(INIT_ROLE_TABLE);
        db.execSQL(INIT_GOAL_TABLE);
        db.execSQL(INIT_TASK_TABLE);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String role = "DROP TABLE IF EXISTS " + TABLE_NAME_ROLE_LIST;
        final String goal = "DROP TABLE IF EXISTS " + TABLE_NAME_GOAL_LIST;
        final String task = "DROP TABLE IF EXISTS " + TABLE_NAME_TASK_LIST;
        Log.d("DBHelper", "onUpgrade");
        // Note: To upgrade table by deleting&creating table is not the best way.
        db.execSQL(role);
        db.execSQL(goal);
        db.execSQL(task);
        this.onCreate(db);
    }
}
