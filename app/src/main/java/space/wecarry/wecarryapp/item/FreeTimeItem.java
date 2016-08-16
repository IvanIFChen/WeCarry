package space.wecarry.wecarryapp.item;

/**
 * Created by Blair on 2016/8/16.
 */
public class FreeTimeItem {

    private long start;
    private long end;
    private long availableTime;

    public FreeTimeItem() {
        new FreeTimeItem(0, 0);
    }

    public FreeTimeItem(long s, long e) {
        this.start = s;
        this.end = e;
        this.availableTime = e-s;
    }

    public long getStart() {
        return this.start;
    }

    public long getEnd() {
        return this.end;
    }

    public long getAvailableTime() {
        return this.availableTime;
    }

    @Override
    public String toString() {
        return "[ start: " + start + ",  end: " + end +",  available: " + availableTime + "]";
    }

}
