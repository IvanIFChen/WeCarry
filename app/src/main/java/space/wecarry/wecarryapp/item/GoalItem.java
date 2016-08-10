package space.wecarry.wecarryapp.item;

import java.util.ArrayList;

/**
 * Created by Blair on 2016/8/5.
 */
public class GoalItem {

    private String title;
    private long deadline;
    private long duration;
    private boolean importance;
    private boolean urgency;
    private ArrayList<TaskItem> taskList;

    public GoalItem() {
        this.title = "";
        this.deadline = 0;
        this.duration = 0;
        this.importance = false;
        this.urgency = false;
        this.taskList = new ArrayList<>();
    }

    public GoalItem(String title, long deadline, long duration, boolean importance, boolean urgency, ArrayList<TaskItem> taskList) {
        this.title = title;
        this.deadline = deadline;
        this.duration = duration;
        this.importance = importance;
        this.urgency = urgency;
        this.taskList = taskList;
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

    public void setUrgency(boolean urgency) {
        this.urgency = urgency;
    }

    public void setImportance(boolean importance) {
        this.importance = importance;
    }

    public String getTitle() {
        return title;
    }

    public void setTaskList(ArrayList<TaskItem> taskList) {
        this.taskList = taskList;
    }

    public void addTaskItem(TaskItem taskItem) {
        this.taskList.add(taskItem);
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
        return "GoalItem{" +
                "title='" + title + '\'' +
                ", deadline=" + deadline +
                ", duration=" + duration +
                ", importance=" + importance +
                ", urgency=" + urgency +
                ", taskList=" + taskList +
                '}';
    }
}
