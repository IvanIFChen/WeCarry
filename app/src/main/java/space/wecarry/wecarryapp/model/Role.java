package space.wecarry.wecarryapp.model;

import java.util.ArrayList;
import space.wecarry.wecarryapp.model.Goal;

/**
 * Created by Ivan IF Chen on 8/9/2016.
 */
public class Role {
    String name;
    ArrayList<Goal> goalList;

    public Role() { }

    public Role(String name, ArrayList<Goal> goalList) {
        this.name = name;
        this.goalList = goalList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Goal> getGoalList() {
        return goalList;
    }

    public void setGoalList(ArrayList<Goal> goalList) {
        this.goalList = goalList;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", goalList=" + goalList +
                '}';
    }

}
