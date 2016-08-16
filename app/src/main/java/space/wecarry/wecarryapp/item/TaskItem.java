package space.wecarry.wecarryapp.item;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        this.deadline = 0;
        this.preprocessList = new ArrayList<TaskItem>();
        this.resourcesList = new ArrayList<ResourceItem>();
    }

    public TaskItem(String title, long processTime) {
        this.title = title;
        this.duration = processTime;
        this.earliestStartTime = 0;
        this.latestStartTime = 0;
        this.earliestEndTime = 0;
        this.latestEndTime = 0;
        this.preprocessList = new ArrayList<TaskItem>();
        this.resourcesList = new ArrayList<ResourceItem>();
    }

    public TaskItem(String title, long deadline, long processTime) {
        this.title = title;
        this.deadline = deadline;
        this.duration = processTime;
    }

    public TaskItem(int goalId, int roleId, String title, long deadline, long duration) {
        this.goalId = goalId;
        this.roleId = roleId;
        this.title = title;
        this.deadline = deadline;
        this.duration = duration;
    }

    // all fields
    public TaskItem(int taskId, int goalId, int roleId, String title, boolean isMilestone, long earliestStartTime, long latestStartTime, long earliestEndTime, long latestEndTime, long deadline, long duration, ArrayList<TaskItem> preprocessList, ArrayList<ResourceItem> resourcesList) {
        this.taskId = taskId;
        this.goalId = goalId;
        this.roleId = roleId;
        this.title = title;
        this.isMilestone = isMilestone;
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

    // output resource titles seperated with commas.
    public String resourceToString() {
        //TODO: incomplete, only store names.
        if (this.resourcesList != null) {
            if (this.resourcesList.size() != 0) {
                String out = "";
                for (ResourceItem ri : this.resourcesList) {
                    out = ri.getTitle() + ", ";
                }
                return out.substring(0, out.lastIndexOf(","));
            }
        }
        return " ";
    }

    public void setResourceFromString(String res) {
        if (res != null && !res.isEmpty()) {
            Log.d("res value", res + " ");
                List<String> items = Arrays.asList(res.split("\\s*,\\s*"));
            // TODO: above ^ only parse names, doesn't save email.
            ArrayList<ResourceItem> resList = new ArrayList<ResourceItem>();
            for (String s : items) {
                ResourceItem ri = new ResourceItem(s, "");
                resList.add(ri);
            }

            // save to field
            this.resourcesList = resList;
        }
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
