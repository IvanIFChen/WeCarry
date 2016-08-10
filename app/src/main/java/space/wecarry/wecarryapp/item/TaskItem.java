package space.wecarry.wecarryapp.item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ivan IF Chen on 8/9/2016.
 */
public class TaskItem implements Serializable {
    private String title;
    private long earliestStartTime;
    private long latestStartTime;
    private long earliestEndTime;
    private long latestEndTime;
    private long deadline;
    private long duration;
    private TaskItem preprocess;
    private ArrayList<ResourceItem> resourcesList;

    public TaskItem() {
        this.title = "";
        this.earliestStartTime = 0;
        this.latestStartTime = 0;
        this.earliestEndTime = 0;
        this.latestEndTime = 0;
        this.duration = 0;
        this.preprocess = new TaskItem();
        this.resourcesList = new ArrayList<>();
    }

    public TaskItem(String title, long processTime) {

        this.title = title;
        this.duration = processTime;
    }

    public TaskItem(String title, long earliestStartTime, long latestStartTime,
                    long earliestEndTime, long latestEndTime, long processTime,
                    TaskItem preprocess, ArrayList<ResourceItem> resourcesList) {
        this.title = title;
        this.earliestStartTime = earliestStartTime;
        this.latestStartTime = latestStartTime;
        this.earliestEndTime = earliestEndTime;
        this.latestEndTime = latestEndTime;
        this.duration = processTime;
        this.preprocess = preprocess;
        this.resourcesList = resourcesList;
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

    public ArrayList<ResourceItem> getResourcesList() {
        return resourcesList;
    }

    public void setResourcesList(ArrayList<ResourceItem> resourcesList) {
        this.resourcesList = resourcesList;
    }

    public void addResourcesItem(ResourceItem resourceItem) {
        this.resourcesList.add(resourceItem);
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "title='" + title + '\'' +
                ", earliestStartTime=" + earliestStartTime +
                ", latestStartTime=" + latestStartTime +
                ", earliestEndTime=" + earliestEndTime +
                ", latestEndTime=" + latestEndTime +
                ", deadline=" + deadline +
                ", duration=" + duration +
                ", preprocess=" + preprocess +
                ", resourcesList=" + resourcesList +
                '}';
    }
}
