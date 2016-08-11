package space.wecarry.wecarryapp;

/**
 * Created by Ivan IF Chen on 8/10/2016.
 */
public class UserConstants {

    public static int sleepTime = 24;                               // in hours
    public static int wakeTime = 8;                                 // in hours
    public static int sleepDuration = 24 - (sleepTime - wakeTime);  // in hours
    public static int breakfast = 8;                                // in hours
    public static int breakfastDuration = 30;                       // in minutes
    public static int lunch = 12;                                   // in hours
    public static int lunchDuration = 60;                           // in minutes
    public static int dinner = 19;                                  // in hours
    public static int dinnerDuration = 60;                          // in minutes

    // TODO: hours -> minutes
}
