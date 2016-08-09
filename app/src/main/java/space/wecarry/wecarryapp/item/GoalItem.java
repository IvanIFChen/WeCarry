package space.wecarry.wecarryapp.item;

import java.util.ArrayList;

/**
 * Created by Blair on 2016/8/5.
 */
public class GoalItem {

    private String text;
    private long deadline;
    private long duration;
    private boolean importance;
    private boolean urgency;
    private ArrayList<TaskItem> taskList;

    public GoalItem() {
        this.text = "";
        this.deadline = 0;
        this.duration = 0;
        this.importance = false;
        this.urgency = false;
    }

    public GoalItem(String text, long deadline, boolean importance, boolean urgency, ArrayList<TaskItem> taskList) {
        this.text = text;
        this.deadline = deadline;
        this.importance = importance;
        this.urgency = urgency;
        this.taskList = taskList;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setUrgency(boolean urgency) {
        this.urgency = urgency;
    }

    public void setImportance(boolean importance) {
        this.importance = importance;
    }

    public String getText() {
        return text;
    }

    public void setTaskList(ArrayList<TaskItem> taskList) {
        this.taskList = taskList;
    }

    public long getDeadline() {
        return deadline;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isImportance() {
        return importance;
    }

    public boolean isUrgency() {
        return urgency;
    }

    public ArrayList<TaskItem> getTaskList() {
        return taskList;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "text='" + text + '\'' +
                ", deadline=" + deadline +
                ", importance=" + importance +
                ", urgency=" + urgency +
                ", taskList=" + taskList +
                '}';
    }
}
