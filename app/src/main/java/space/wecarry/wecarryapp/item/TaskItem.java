package space.wecarry.wecarryapp.item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ivan IF Chen on 8/9/2016.
 */
public class TaskItem implements Serializable {
    private int taskId;
    private int goalId;
    private int roleId;
    private String title;
    private boolean isMilestone;
    private long earliestStartTime;
    private long latestStartTime;
    private long earliestEndTime;
    private long latestEndTime;
    private long deadline;
    private long duration; /// in milliseconds
    private ArrayList<TaskItem> preprocessList;
    private ArrayList<ResourceItem> resourcesList;

    public TaskItem() {
        this.taskId = -1;
        this.goalId = -1;
        this.roleId = -1;
        this.isMilestone = false;
        this.title = "";
        this.earliestStartTime = 0;
        this.latestStartTime = 0;
        this.earliestEndTime = 0;
        this.latestEndTime = 0;
        this.duration = 0;
        this.preprocessList = new ArrayList<TaskItem>();
        this.resourcesList = new ArrayList<>();
    }

    public TaskItem(String title, long processTime) {
        this.title = title;
        this.duration = processTime;
        this.earliestStartTime = 0;
        this.latestStartTime = 0;
        this.earliestEndTime = 0;
        this.latestEndTime = 0;
        this.preprocessList = new ArrayList<TaskItem>();
        this.resourcesList = new ArrayList<>();
    }

    public TaskItem(String title, long deadline, long processTime) {
        this.title = title;
        this.deadline = deadline;
        this.duration = processTime;
//        this.latestEndTime = deadline;
//        this.latestStartTime = deadline - processTime;

    }

    public TaskItem(String title, long earliestStartTime, long latestStartTime, long earliestEndTime, long latestEndTime, long deadline, long duration, ArrayList<TaskItem> preprocessList, ArrayList<ResourceItem> resourcesList) {
        this.title = title;
        this.earliestStartTime = earliestStartTime;
        this.latestStartTime = latestStartTime;
        this.earliestEndTime = earliestEndTime;
        this.latestEndTime = latestEndTime;
        this.deadline = deadline;
        this.duration = duration;
        this.preprocessList = preprocessList;
        this.resourcesList = resourcesList;
    }

    public TaskItem(int id, String title, long earliestStartTime, long latestStartTime, long earliestEndTime, long latestEndTime, long deadline, long duration, ArrayList<TaskItem> preprocessList, ArrayList<ResourceItem> resourcesList) {
        this.taskId = id;
        this.title = title;
        this.earliestStartTime = earliestStartTime;
        this.latestStartTime = latestStartTime;
        this.earliestEndTime = earliestEndTime;
        this.latestEndTime = latestEndTime;
        this.deadline = deadline;
        this.duration = duration;
        this.preprocessList = preprocessList;
        this.resourcesList = resourcesList;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isMilestone() {
        return isMilestone;
    }

    public void setMilestone(boolean milestone) {
        isMilestone = milestone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getEarliestStartTime() {
        return earliestStartTime;
    }

    public void setEarliestStartTime(long earliestStartTime) {
        this.earliestStartTime = earliestStartTime;
    }

    public long getLatestStartTime() {
        return latestStartTime;
    }

    public void setLatestStartTime(long latestStartTime) {
        this.latestStartTime = latestStartTime;
    }

    public long getEarliestEndTime() {
        return earliestEndTime;
    }

    public void setEarliestEndTime(long earliestEndTime) {
        this.earliestEndTime = earliestEndTime;
    }

    public long getLatestEndTime() {
        return latestEndTime;
    }

    public void setLatestEndTime(long latestEndTime) {
        this.latestEndTime = latestEndTime;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public ArrayList<TaskItem> getPreprocessList() {
        return preprocessList;
    }

    public void setPreprocessList(ArrayList<TaskItem> preprocessList) {
        this.preprocessList = preprocessList;
    }

    public ArrayList<ResourceItem> getResourcesList() {
        return resourcesList;
    }

    public void setResourcesList(ArrayList<ResourceItem> resourcesList) {
        this.resourcesList = resourcesList;
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "taskId=" + taskId +
                ", goalId=" + goalId +
                ", roleId=" + roleId +
                ", title='" + title + '\'' +
                ", isMilestone=" + isMilestone +
                ", earliestStartTime=" + earliestStartTime +
                ", latestStartTime=" + latestStartTime +
                ", earliestEndTime=" + earliestEndTime +
                ", latestEndTime=" + latestEndTime +
                ", deadline=" + deadline +
                ", duration=" + duration +
                ", preprocessList=" + preprocessList +
                ", resourcesList=" + resourcesList +
                '}';
    }

}
