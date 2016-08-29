package space.wecarry.wecarryapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;
import space.wecarry.wecarryapp.item.TaskItem;

/**
 * Created by Ivan IF Chen on 8/27/2016.
 */
public class ListDataConverter {
    public static LinkedHashMap<String, List<String>> convertRoleListData(ArrayList<RoleItem> roleList) {
        /**
         * Input should be either a roleList (for the Role and Goal fragment)
         * or a goalList (used for the Plan fragment).
         * Example:
         * roleList:
         * +-------+---------+
         * | role1 | goal1-1 |
         * |       | goal1-2 |
         * |       | goal1-3 |
         * | ----  | ------- |
         * | role2 | goal2-1 |
         * |       | goal2-2 |
         * | ----  | ------- |
         * | role3 | goal3-1 |
         * +-------+---------+
         *
         * goalList:
         * +-------+---------+
         * | goal1 | task1-1 |
         * |       | task1-2 |
         * |       | task1-3 |
         * | ----  | ------- |
         * | goal2 | task2-1 |
         * |       | task2-2 |
         * | ----  | ------- |
         * | goal3 | task3-1 |
         * +-------+---------+
         */

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        // Convert roleList to HashMap.
        for (RoleItem ri : roleList) {
            List<String> detailList = new ArrayList<String>();

            // adds all gi's title
            for (GoalItem gi : ri.getGoalList()) {
                detailList.add(gi.getTitle());
            }

            // put key (role title) and value (titles of role's goals) in HashMap
            expandableListDetail.put(ri.getTitle(), detailList);
        }

        return expandableListDetail;
    }

    public static LinkedHashMap<String, List<String>> convertGoalListData(ArrayList<GoalItem> goalList) {
        /**
         * Input should be either a roleList (for the Role and Goal fragment)
         * or a goalList (used for the Plan fragment).
         * Example:
         * roleList:
         * +-------+---------+
         * | role1 | goal1-1 |
         * |       | goal1-2 |
         * |       | goal1-3 |
         * | ----  | ------- |
         * | role2 | goal2-1 |
         * |       | goal2-2 |
         * | ----  | ------- |
         * | role3 | goal3-1 |
         * +-------+---------+
         *
         * goalList:
         * +-------+---------+
         * | goal1 | task1-1 |
         * |       | task1-2 |
         * |       | task1-3 |
         * | ----  | ------- |
         * | goal2 | task2-1 |
         * |       | task2-2 |
         * | ----  | ------- |
         * | goal3 | task3-1 |
         * +-------+---------+
         */

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        // Convert roleList to HashMap.
        for (GoalItem gi : goalList) {
            List<String> detailList = new ArrayList<String>();

            // adds all ti's title
            for (TaskItem ti : gi.getTaskList()) {
                detailList.add(ti.getTitle());
            }

            // put key (goal title) and value (titles of goal's tasks) in HashMap
            expandableListDetail.put(gi.getTitle(), detailList);
        }

        return expandableListDetail;
    }

}
