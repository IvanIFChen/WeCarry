package space.wecarry.wecarryapp.item;

/**
 * Created by Ivan IF Chen on 8/9/2016.
 */
public class TaskItem {
    String name;
    double earliestStartTime;
    double latestStartTime;
    double earliestEndTime;
    double latestEndTime;
    double processTime;
    TaskItem preprocess;
    String resources;

    public TaskItem() { }

    public TaskItem(String name, double processTime) {
    
        this.name = name;
        this.processTime = processTime;
    }

    public TaskItem(String name, double earliestStartTime, double latestStartTime,
                    double earliestEndTime, double latestEndTime, double processTime,
                    TaskItem preprocess, String resources) {
        this.name = name;
        this.earliestStartTime = earliestStartTime;
        this.latestStartTime = latestStartTime;
        this.earliestEndTime = earliestEndTime;
        this.latestEndTime = latestEndTime;
        this.processTime = processTime;
        this.preprocess = preprocess;
        this.resources = resources;
    }

    public String getName() {
    
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEarliestStartTime() {
        return earliestStartTime;
    }

    public void setEarliestStartTime(double earliestStartTime) {
        this.earliestStartTime = earliestStartTime;
    }

    public double getLatestStartTime() {
        return latestStartTime;
    }

    public void setLatestStartTime(double latestStartTime) {
        this.latestStartTime = latestStartTime;
    }

    public double getEarliestEndTime() {
        return earliestEndTime;
    }

    public void setEarliestEndTime(double earliestEndTime) {
        this.earliestEndTime = earliestEndTime;
    }

    public double getLatestEndTime() {
        return latestEndTime;
    }

    public void setLatestEndTime(double latestEndTime) {
        this.latestEndTime = latestEndTime;
    }

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    public TaskItem getPreprocess() {
        return preprocess;
    }

    public void setPreprocess(TaskItem preprocess) {
        this.preprocess = preprocess;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", earliestStartTime=" + earliestStartTime +
                ", latestStartTime=" + latestStartTime +
                ", earliestEndTime=" + earliestEndTime +
                ", latestEndTime=" + latestEndTime +
                ", processTime=" + processTime +
                ", preprocess=" + preprocess +
                ", resources='" + resources + '\'' +
                '}';
    }
}
