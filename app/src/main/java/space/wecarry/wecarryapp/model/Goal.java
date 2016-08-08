package space.wecarry.wecarryapp.model;

import java.util.ArrayList;
import space.wecarry.wecarryapp.model.Task;

/**
 * Created by Ivan IF Chen on 8/9/2016.
 */
public class Goal {
    String name;
    double deadline;
    boolean importance;
    boolean urgency;
    ArrayList<Task> taskList;

    public Goal() { }

    public Goal(String name, double deadline, boolean importance, boolean urgency, ArrayList<Task> taskList) {

        this.name = name;
        this.deadline = deadline;
        this.importance = importance;
        this.urgency = urgency;
        this.taskList = taskList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDeadline() {
        return deadline;
    }

    public void setDeadline(double deadline) {
        this.deadline = deadline;
    }

    public boolean isImportance() {
        return importance;
    }

    public void setImportance(boolean importance) {
        this.importance = importance;
    }

    public boolean isUrgency() {
        return urgency;
    }

    public void setUrgency(boolean urgency) {
        this.urgency = urgency;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "name='" + name + '\'' +
                ", deadline=" + deadline +
                ", importance=" + importance +
                ", urgency=" + urgency +
                ", taskList=" + taskList +
                '}';
    }
}
