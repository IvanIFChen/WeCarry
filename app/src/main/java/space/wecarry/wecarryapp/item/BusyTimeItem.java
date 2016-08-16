package space.wecarry.wecarryapp.item;

/**
 * Created by Blair on 2016/8/16.
 */
public class BusyTimeItem {

    private long start;
    private long end;

    public BusyTimeItem() {
        new BusyTimeItem(0, 0);
    }

    public BusyTimeItem(long s, long e) {
        this.start = s;
        this.end = e;
    }

    public long getStart() {
        return this.start;
    }

    public long getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "[ start: " + start + ",  end: " + end + "]";
    }

}
