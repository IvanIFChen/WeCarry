package space.wecarry.wecarryapp.item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Blair on 2016/8/5.
 */
public class GoalItem implements Serializable {

    private String title;
    private long deadline;
    private long duration;
    private boolean isImportant;
    private boolean isUrgent;
    private ArrayList<TaskItem> taskList;

    public GoalItem() {
        this.title = "";
        this.deadline = 0;
        this.duration = 0;
        this.isImportant = false;
        this.isUrgent = false;
        this.taskList = new ArrayList<>();
    }

    public GoalItem(String title, long deadline, boolean isImportant, boolean isUrgent, ArrayList<TaskItem> taskList) {
        this.title = title;
        this.deadline = deadline;
        this.isImportant = isImportant;
        this.isUrgent = isUrgent;
        this.taskList = taskList;
        calculateTotalDuration();
    }

//    public GoalItem(String title, long deadline, long duration, boolean isImportant, boolean isUrgent, ArrayList<TaskItem> taskList) {
//        this.title = title;
//        this.deadline = deadline;
//        this.duration = duration;
//        this.isImportant = isImportant;
//        this.isUrgent = isUrgent;
//        this.taskList = taskList;
//    }

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
        calculateTotalDuration();
    }

    public void addTaskItem(TaskItem taskItem) {
        this.taskList.add(taskItem);
        calculateTotalDuration();
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

    private void calculateTotalDuration() {
        long sum = 0;
        for (TaskItem ti : this.taskList) {
            sum = sum + ti.getDuration();
        }
        this.duration = sum;
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
