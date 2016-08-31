package space.wecarry.wecarryapp.sqlite;

/**
 * Created by Ivan IF Chen on 8/9/2016.
 */
public class DBConstants {

    public static final String TABLE_NAME_ROLE_LIST = "ROLE_LIST";
    public static final String TABLE_NAME_GOAL_LIST = "GOAL_LIST";
    public static final String TABLE_NAME_TASK_LIST = "TASK_LIST";
    public static final String TABLE_NAME_RESOURCE_LIST = "RESOURCE_LIST";
    public static final String TABLE_NAME_SCHEDULE_LIST = "SCHEDULE_LIST";
    public static final String TABLE_NAME_USER_CONSTANTS = "TABLE_NAME_USER_CONSTANTS";

    // Role
    public static final String ROLE_ID = "ROLE_ID";
    public static final String ROLE_TITLE = "ROLE_TITLE";
    public static final String ROLE_DEADLINE = "ROLE_DEADLINE";
    public static final String ROLE_DURATION = "ROLE_DURATION";

    // Goal
    public static final String GOAL_ID = "GOAL_ID";
    public static final String GOAL_TITLE = "GOAL_TITLE";
    public static final String GOAL_DEADLINE = "GOAL_DEADLINE";
    public static final String GOAL_DURATION = "GOAL_DURATION";
    public static final String GOAL_IMPORTANCE = "GOAL_IMPORTANCE";
    public static final String GOAL_URGENCY = "GOAL_URGENCY";
    public static final String GOAL_ROLE_ID = "GOAL_ROLE_ID";

    // Task
    public static final String TASK_ID = "TASK_ID";
    public static final String TASK_TITLE = "TASK_TITLE";
    public static final String TASK_MILESTONE = "TASK_MILESTONE";
    public static final String TASK_EST = "TASK_EST";
    public static final String TASK_LST = "TASK_LST";
    public static final String TASK_EET = "TASK_EET";
    public static final String TASK_LET = "TASK_LET";
    public static final String TASK_DEADLINE = "TASK_DEADLINE";
    public static final String TASK_DURATION = "TASK_PROCESS_TIME";
    public static final String TASK_PREPROCESS = "TASK_PREPROCESS";
    public static final String TASK_RESOURCE = "TASK_RESOURCE";
    public static final String TASK_GOAL_ID = "TASK_GOAL_ID";
    public static final String TASK_ROLE_ID = "TASK_ROLE_ID";
    public static final String TASK_IMPORTANCE = "TASK_IMPORTANCE";
    public static final String TASK_URGENCY = "TASK_URGENCY";

    // Resource
    public static final String RESOURCE_ID = "RESOURCE_ID";
    public static final String RESOURCE_TITLE = "RESOURCE_TITLE";
    public static final String RESOURCE_EMAIL = "RESOURCE_EMAIL";
    public static final String RESOURCE_TASK_ID = "RESOURCE_TASK_ID";
    public static final String RESOURCE_GOAL_ID = "RESOURCE_GOAL_ID";
    public static final String RESOURCE_ROLE_ID = "RESOURCE_ROLE_ID";

    // Schedule
    public static final String SCHEDULE_TASK_ID = "SCHEDULE_TASK_ID";
    public static final String SCHEDULE_EVENT_ID = "SCHEDULE_EVENT_ID";

    // User Constants
    public static final String USER_SLEEP_TIME = "USER_SLEEP_TIME";
    public static final String USER_WAKE_TIME = "USER_WAKE_TIME";
    public static final String USER_BREAKFAST_TIME = "USER_BREAKFAST_TIME";
    public static final String USER_BREAKFAST_DURATION = "USER_BREAKFAST_DURATION";
    public static final String USER_LUNCH_TIME = "USER_LUNCH_TIME";
    public static final String USER_LUNCH_DURATION = "USER_LUNCH_DURATION";
    public static final String USER_DINNER_TIME = "USER_DINNER_TIME";
    public static final String USER_DINNER_DURATION = "USER_DINNER_DURATION";

//    public static String LOCAL_DB_VERSION = "";
//    public static String SERVER_DB_VERSION = "";

}
