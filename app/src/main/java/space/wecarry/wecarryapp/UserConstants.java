package space.wecarry.wecarryapp;

/**
 * Created by Ivan IF Chen on 8/10/2016.
 */
public class UserConstants {
    // in minutes
    public static int sleepTime = hourToMinute(24);
    public static int wakeTime = hourToMinute(8);
    public static int sleepDuration =
            hourToMinute(24) - (sleepTime - wakeTime);
    public static int breakfast = hourToMinute(8);
    public static int breakfastDuration = 30;
    public static int lunch = hourToMinute(12);
    public static int lunchDuration = 60;
    public static int dinner = hourToMinute(19);
    public static int dinnerDuration = 60;

    private static int hourToMinute(int hour) {
        return hour * 60;
    }
}
