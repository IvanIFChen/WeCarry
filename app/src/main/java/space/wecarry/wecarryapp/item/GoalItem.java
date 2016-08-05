package space.wecarry.wecarryapp.item;

/**
 * Created by Blair on 2016/8/5.
 */
public class GoalItem {

    private String text;
    private long deadline;
    private long duration;
    private boolean importance;
    private boolean urgency;

    public GoalItem() {
        this.text = "";
        this.deadline = 0;
        this.duration = 0;
        this.importance = false;
        this.urgency = false;
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

    public void setUrgency(boolean urgency) {
        this.urgency = urgency;
    }

    public void setImportance(boolean importance) {
        this.importance = importance;
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

    public boolean isImportance() {
        return importance;
    }

    public boolean isUrgency() {
        return urgency;
    }

    @Override
    public String toString() {
        return "[ text: " + text + ",  deadline: " + deadline +",  duration: " + duration +",  importance: " + importance + ", urgency: " + urgency + "]" ;
    }
}
