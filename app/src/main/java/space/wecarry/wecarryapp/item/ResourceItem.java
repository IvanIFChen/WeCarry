package space.wecarry.wecarryapp.item;

import java.io.Serializable;

/**
 * Created by Blair on 2016/8/9.
 */
public class ResourceItem implements Serializable {
    private int resourceId;
    private int taskId;
    private int goalId;
    private int roleId;
    private String title;
    private String email;

    public ResourceItem() {
        this.resourceId = -1;
        this.taskId = -1;
        this.goalId = -1;
        this. roleId = -1;
        this.title = "";
        this.email = "";
    }

    public ResourceItem(String title, String email) {
        this.title = title;
        this.email = email;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ResourceItem{" +
                "resourceId=" + resourceId +
                ", taskId=" + taskId +
                ", goalId=" + goalId +
                ", roleId=" + roleId +
                ", title='" + title + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
