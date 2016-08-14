package space.wecarry.wecarryapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;
import space.wecarry.wecarryapp.item.TaskItem;

/**
 * Created by Ivan IF Chen on 8/14/2016.
 */
public class DemoData {
    boolean showInWeek;
    private ArrayList<RoleItem> roleList;
    private ArrayList<GoalItem> goalList;
    private ArrayList<TaskItem> taskList;

    ArrayList<TaskItem> g1Tasks = new ArrayList<TaskItem>();
    ArrayList<TaskItem> g2Tasks = new ArrayList<TaskItem>();
    ArrayList<TaskItem> g3Tasks = new ArrayList<TaskItem>();
    ArrayList<TaskItem> g4Tasks = new ArrayList<TaskItem>();
    ArrayList<TaskItem> g5Tasks = new ArrayList<TaskItem>();
    ArrayList<GoalItem> r1Goals = new ArrayList<GoalItem>();
    ArrayList<GoalItem> r2Goals = new ArrayList<GoalItem>();

    public DemoData(Boolean showInWeek) {
        this.showInWeek = showInWeek;
        // when create this class (object) it will generate the data.
        generateDemoData();
        // now the get methods should work
    }

    private void generateDemoData() {
        // clear
        roleList = new ArrayList<RoleItem>();
        goalList = new ArrayList<GoalItem>();
        taskList = new ArrayList<TaskItem>();

        // generate tasks without deadline and add them to goalTasks and taskList
        generateTasks();

        // initialize goal items and their tasks (without deadline).
        GoalItem g1 = new GoalItem("goal1", 0, true, true, g1Tasks); // will add deadline later
        GoalItem g2 = new GoalItem("goal2", 0, true, false, g2Tasks);
        GoalItem g3 = new GoalItem("goal3", 0, false, true, g3Tasks);
        GoalItem g4 = new GoalItem("goal4", 0, false, false, g4Tasks);
        GoalItem g5 = new GoalItem("goal5", 0, true, false, g5Tasks);

        // add goals to goal lists
        r1Goals.add(g1);
        r1Goals.add(g2);
        r1Goals.add(g3);
        r2Goals.add(g4);
        r2Goals.add(g5);

        // initialize role items and their goals.
        RoleItem r1 = new RoleItem("role1", r1Goals);
        RoleItem r2 = new RoleItem("role2", r2Goals);

        // add roles to roleList.
        roleList.add(r1);
        roleList.add(r2);

        // add goals to goalList.
        goalList.add(g1);
        goalList.add(g2);
        goalList.add(g3);
        goalList.add(g4);
        goalList.add(g5);

        /**
         * Add deadline for goal and their tasks:
         * It will add one task from each goal to one day.
         * So if we have 2goals and 10 tasks for each goal (total of 20 tasks),
         * it will add two tasks (one from each goal) for each day, for the past
         * 10 days.
         */
        for (GoalItem gi : goalList) {
            Calendar cal = new GregorianCalendar();
            gi.setDeadline(cal.getTimeInMillis());
//            Log.d("Today", "   " + Long.toString(cal.getTimeInMillis()));
            for (TaskItem ti : gi.getTaskList()) {
                cal.add(cal.DATE, -1);
//                Log.d("Today -1", cal.toString());
                ti.setDeadline(cal.getTimeInMillis());
            }
        }

        Log.d("Test: checkQuadrant", Long.toString(g1.checkQuadrant()));
        Log.d("Test: checkQuadrant", Long.toString(g2.checkQuadrant()));
        Log.d("Test: checkQuadrant", Long.toString(g3.checkQuadrant()));
        Log.d("Test: checkQuadrant", Long.toString(g4.checkQuadrant()));
        Log.d("Test: checkQuadrant", Long.toString(g5.checkQuadrant()));

        Log.d("Test: totalDuration", Long.toString(g1.getDuration()));
        Log.d("Test: totalDuration", Long.toString(g2.getDuration()));
        Log.d("Test: totalDuration", Long.toString(g3.getDuration()));
        Log.d("Test: totalDuration", Long.toString(g4.getDuration()));
        Log.d("Test: totalDuration", Long.toString(g5.getDuration()));
    }

    private void generateTasks() {
        // This method will generate tasks without deadlines.
        int nameCount = 1;
        int sampleSize;
        if (showInWeek)
            sampleSize = 10;
        else
            sampleSize = 40;
        Math.random();
        for (int i = 1; i <= sampleSize; i++) {
            TaskItem task;
            task = new TaskItem("task" + nameCount,
                    60 * 100 * randomValue());
//            Log.e("task", task.toString());
            g1Tasks.add(task);
            taskList.add(task);
            nameCount = nameCount + 1;
            task = new TaskItem("task" + nameCount,
                    60 * 100 * randomValue());
//            Log.e("task", task.toString());
            g2Tasks.add(task);
            taskList.add(task);
            nameCount = nameCount + 1;
            task = new TaskItem("task" + nameCount,
                    60 * 100 * randomValue());
//            Log.e("task", task.toString());
            g3Tasks.add(task);
            taskList.add(task);
            nameCount = nameCount + 1;
            task = new TaskItem("task" + nameCount,
                    60 * 100 * randomValue());
//            Log.e("task", task.toString());
            g4Tasks.add(task);
            taskList.add(task);
            nameCount = nameCount + 1;
            task = new TaskItem("task" + nameCount,
                    60 * 100 * randomValue());
//            Log.e("task", task.toString());
            g5Tasks.add(task);
            taskList.add(task);
        }
    }

    private long randomValue() {
        // random value = (max ~1.5hr, min ~.5hr)
        // in milliseconds
        return (long) (Math.random() * 200); // unpredictable person
//        return (long) (Math.random() * 70 + 30); // lazy person
    }

    public ArrayList<RoleItem> getGeneratedRoleList() {
        if (roleList.size() == 0) {
            Log.e("DemoData", "Generate the data first!");
        }
        return this.roleList;
    }
    public ArrayList<GoalItem> getGeneratedGoalList() {
        if (goalList.size() == 0) {
            Log.e("DemoData", "Generate the data first!");
        }
        return this.goalList;
    }
    public ArrayList<TaskItem> getGeneratedTaskList() {
        if (taskList.size() == 0) {
            Log.e("DemoData", "Generate the data first!");
        }
        return this.taskList;
    }
}
