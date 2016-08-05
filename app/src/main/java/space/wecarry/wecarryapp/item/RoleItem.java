package space.wecarry.wecarryapp.item;

import java.util.ArrayList;

import space.wecarry.wecarryapp.item.GoalItem;

/**
 * Created by Blair on 2016/8/5.
 */
public class RoleItem {

    private String text;
    private long deadline;
    private long duration;
    ArrayList<GoalItem> goalList;

    public RoleItem() {
        this.text = "";
        this.deadline = 0;
        this.duration = 0;
        this.goalList = new ArrayList<>();
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

    public void setGoalList(ArrayList goalList) {
        this.goalList = goalList;
    }

    public void addGoalItem(GoalItem goalItem) {
        this.goalList.add(goalItem);
    }

    public String getText() {
        return text;
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
        return "[ text: " + text + ",  deadline: " + deadline +",  duration: " + duration +
                ",  goal num: " + Integer.toString(goalList.size()) +"]" ;
    }
}
