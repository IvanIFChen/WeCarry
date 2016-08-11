package space.wecarry.wecarryapp.item;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Blair on 2016/8/5.
 */
public class GoalItem implements Serializable {

    private int id; // We use it to make determine whether to modify or add data in DB. // Default: -1 , It means "add".
    private String title;
    private long deadline;
    private long duration;
    private boolean isImportant;
    private boolean isUrgent;
    private ArrayList<TaskItem> taskList;

    public GoalItem() {
        this.id = -1;   // We use it to make determine whether to modify or add data in DB. // Default: -1 , It means "add".
        this.title = "";
        this.deadline = 0;
        this.duration = 0;
        this.isImportant = false;
        this.isUrgent = false;
        this.taskList = new ArrayList<>();
    }

    public GoalItem(String title, long deadline, boolean isImportant, boolean isUrgent, ArrayList<TaskItem> taskList) {
//        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.isImportant = isImportant;
        this.isUrgent = isUrgent;
        this.taskList = taskList;
        this.duration = 0;
        updateDuration();
    }

//    public GoalItem(String title, long deadline, long duration, boolean isImportant, boolean isUrgent, ArrayList<TaskItem> taskList) {

    //    }
//        this.taskList = taskList;
//        this.isUrgent = isUrgent;
//        this.isImportant = isImportant;
//        this.duration = duration;
//        this.deadline = deadline;
//        this.title = title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setUrgent(boolean urgent) {
        this.isUrgent = urgent;
    }

    public void setImportant(boolean important) {
        this.isImportant = important;
    }

    public String getTitle() {
        return title;
    }

    public void setTaskList(ArrayList<TaskItem> taskList) {
        this.taskList = taskList;
        updateDuration();
    }

    public void addTaskItem(TaskItem taskItem) {
        this.taskList.add(taskItem);
        updateDuration();
    }

    public long getDeadline() {
        return deadline;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public ArrayList<TaskItem> getTaskList() {
        return taskList;
    }

    private void updateDuration() {
        long sum = 0;
        for (TaskItem ti : this.taskList) {
            sum = sum + ti.getDuration();
        }
        this.duration = sum;
    }

    public int checkQuadrant() {
        if (this.isImportant && this.isUrgent) {
            return 1;
        }
        else if (this.isImportant && !this.isUrgent) {
            return 2;
        }
        else if (!this.isImportant && this.isUrgent) {
            return 3;
        }
        else {
            return 4;
        }
    }

    @Override
    public String toString() {
        return "GoalItem{" +
                "title='" + title + '\'' +
                ", deadline=" + deadline +
                ", duration=" + duration +
                ", isImportant=" + isImportant +
                ", isUrgent=" + isUrgent +
                ", taskList=" + taskList +
                '}';
    }
}
