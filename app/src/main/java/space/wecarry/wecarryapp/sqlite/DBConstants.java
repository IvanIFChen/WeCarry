package space.wecarry.wecarryapp.sqlite;

/**
 * Created by Ivan IF Chen on 8/9/2016.
 */
public class DBConstants {

    public static final String TABLE_NAME_ROLE_LIST = "ROLE_LIST";
    public static final String TABLE_NAME_GOAL_LIST = "GOAL_LIST";
    public static final String TABLE_NAME_TASK_LIST = "TASK_LIST";

    // Role
    public static final String ROLE_ID = "role_id";
    public static final String ROLE_NAME = "role_name";

    // Goal
    public static final String GOAL_ID = "GOAL_ID";
    public static final String GOAL_NAME = "GOAL_NAME";
    public static final String GOAL_DEADLINE = "GOAL_DEADLINE";
    public static final String GOAL_IMPORTANCE = "GOAL_IMPORTANCE";
    public static final String GOAL_URGENCY = "GOAL_URGENCY";
    public static final String GOAL_ROLE_ID = "GOAL_ROLE_ID";

    // Task
    public static final String TASK_ID = "TASK_ID";
    public static final String TASK_NAME = "TASK_NAME";
    public static final String TASK_EST = "TASK_EST";
    public static final String TASK_LST = "TASK_LST";
    public static final String TASK_EET = "TASK_EET";
    public static final String TASK_LET = "TASK_LET";
    public static final String TASK_PROCESS_TIME = "TASK_PROCESS_TIME";
    public static final String TASK_PREPROCESS = "TASK_PREPROCESS";
    public static final String TASK_RESOURCE = "TASK_RESOURCE";
    public static final String TASK_GOAL_ID = "TASK_GOAL_ID";

//    public static String LOCAL_DB_VERSION = "";
//    public static String SERVER_DB_VERSION = "";

}