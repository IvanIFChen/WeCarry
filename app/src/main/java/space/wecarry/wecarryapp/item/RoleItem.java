package space.wecarry.wecarryapp.item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Blair on 2016/8/5.
 */
public class RoleItem implements Serializable {

    private static final long serialVersionUID = 7382351359868556980L;
    private String title;
    private long deadline;
    private long duration;
    private ArrayList<GoalItem> goalList;

    public RoleItem(String title, ArrayList<GoalItem> goalList) {
        this.title = title;
        this.goalList = goalList;
    }

    public RoleItem() {
        this.title = "";
        this.deadline = 0;
        this.duration = 0;
        this.goalList = new ArrayList<>();
    }

    public RoleItem(String title, long deadline, long duration, ArrayList<GoalItem> goalList) {
        this.title = title;
        this.deadline = deadline;
        this.duration = duration;
        this.goalList = goalList;
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

    public void setGoalList(ArrayList<GoalItem> goalList) {
        this.goalList = goalList;
    }

    public void addGoalItem(GoalItem goalItem) {
        this.goalList.add(goalItem);
    }

    public String getTitle() {
        return title;
    }

    public long getDeadline() {
        return deadline;
    }

    public long getDuration() {
        return duration;
    }

    public ArrayList<GoalItem> getGoalList() {
        return goalList;
    }

    public int getGoalNum() {
        return goalList.size();
    }

    // TODO: Remove, Update GoalItem

    @Override
    public String toString() {
        return "[ title: " + title + ",  deadline: " + deadline +",  duration: " + duration +
                ",  goal num: " + Integer.toString(goalList.size()) +"]" ;
    }
}
