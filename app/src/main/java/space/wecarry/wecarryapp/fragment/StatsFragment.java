package space.wecarry.wecarryapp.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import space.wecarry.wecarryapp.DemoData;
import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.UserConstants;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;
import space.wecarry.wecarryapp.item.TaskItem;
import space.wecarry.wecarryapp.listviewitems.ChartItem;
import space.wecarry.wecarryapp.listviewitems.LineChartItem;
import space.wecarry.wecarryapp.listviewitems.PieChartItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

public class StatsFragment extends Fragment {
    private DBHelper dbHelper;
    private ArrayList<RoleItem> roleList;
    private ArrayList<GoalItem> goalList;
    private ArrayList<TaskItem> taskList;
    private boolean showInWeek = true;
    // constants (all in minutes)
    private final float sleepDuration = UserConstants.sleepDuration;
    private final float breakfastDuration = UserConstants.breakfastDuration;
    private final float lunchDuration = UserConstants.lunchDuration;
    private final float dinnerDuration = UserConstants.dinnerDuration;
    private final float personalTime = (sleepDuration +
            breakfastDuration +
            lunchDuration +
            dinnerDuration) /60; // in hours
    // pie data
    private float q1Pie = 0; // in hours
    private float q2Pie = 0;
    private float q3Pie = 0;
    private float q4Pie = 0;
    private float othersPie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_stats));

        // initialize chart library.
        Utils.init(getActivity());

        // TODO: change this to false to show real data.
        boolean showDemo = true;

        if(showDemo) {
            // generate demo data
            DemoData dd = new DemoData(showInWeek);
            roleList = dd.getGeneratedRoleList();
            goalList = dd.getGeneratedGoalList();
            taskList = dd.getGeneratedTaskList();
        } else {
            // get real data
            getDataFromDB();
        }

        ListView lv = (ListView) rootView.findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        list.add(new LineChartItem(generateDataLine(5), getActivity()));
        list.add(new PieChartItem(generateDataPie(1), getActivity()));

        ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
        lv.setAdapter(cda);

        setHasOptionsMenu(true);

        return rootView;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment frg;
        frg = getFragmentManager().findFragmentById(R.id.mainContainer);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.action_week :
                this.showInWeek = true;
                // refresh the fragment
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
                break;
            case R.id.action_month :
                this.showInWeek = false;
                // refresh the fragment
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
                break;
        }
        return true;
    }

    private void getDataFromDB() {
        // clear
        roleList = new ArrayList<RoleItem>();
        goalList = new ArrayList<GoalItem>();
        taskList = new ArrayList<TaskItem>();
        // this part of code is not tested
        DBHelper dbHelper = new DBHelper(getActivity());
        goalList = dbHelper.getAllGoal();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.menu_stats,menu);
    }

    /** adapter that supports 2 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 2; // we have 2 different item-types
        }
    }

    private void clearVariables() {
        q1Pie = 0;
        q2Pie = 0;
        q3Pie = 0;
        q4Pie = 0;
    }

    // append list2 to list1
    private <E> ArrayList<E> appendList(ArrayList<E> list1, ArrayList<E> list2) {
        for (E e : list2) {
            list1.add(e);
        }
        return list1;
    }

    private ArrayList<ArrayList<TaskItem>> sortGoalListToQuadrants() {
        ArrayList<TaskItem> q1List = new ArrayList<TaskItem>();
        ArrayList<TaskItem> q2List = new ArrayList<TaskItem>();
        ArrayList<TaskItem> q3List = new ArrayList<TaskItem>();
        ArrayList<TaskItem> q4List = new ArrayList<TaskItem>();
        ArrayList<ArrayList<TaskItem>> result = new ArrayList<ArrayList<TaskItem>>();

        // cleat variables
        clearVariables();
        // sort tasks to quadrants
        for (GoalItem gi : goalList) {
            switch (gi.checkQuadrant()) {
                case 1:
                    q1List = appendList(q1List, gi.getTaskList());
                    break;
                case 2:
                    q2List = appendList(q2List, gi.getTaskList());
                    break;
                case 3:
                    q3List = appendList(q3List, gi.getTaskList());
                    break;
                case 4:
                    q4List = appendList(q4List, gi.getTaskList());
                    break;
            }
        }
        result.add(q1List);
        result.add(q2List);
        result.add(q3List);
        result.add(q4List);
        return result;
    }

    private void calcQuadrantsForPie() {
        float pt;

        // clear variables
        clearVariables();
        // calc each quadrants
        for (GoalItem gi : goalList) {
            switch (gi.checkQuadrant()) {
                case 1:
                    q1Pie = q1Pie + (float) (gi.getDuration() / 100 / 60 / 60);
                    break;
                case 2:
                    q2Pie = q2Pie + (float) (gi.getDuration() / 100 / 60 / 60);
                    break;
                case 3:
                    q3Pie = q3Pie + (float) (gi.getDuration() / 100 / 60 / 60);
                    break;
                case 4:
                    q4Pie = q4Pie + (float) (gi.getDuration() / 100 / 60 / 60);
                    break;
            }
        }
        Log.i("q1Pie value", Float.toString(q1Pie));
        Log.i("q2Pie value", Float.toString(q2Pie));
        Log.i("q3Pie value", Float.toString(q3Pie));
        Log.i("q4Pie value", Float.toString(q4Pie));
        // calc personal time and othersPie according to period
        if (showInWeek) {
            pt = personalTime * 7;
            othersPie = (7 * 24) - (q1Pie + q2Pie + q3Pie + q4Pie + pt);
        } else {
            pt = personalTime * 30;
            othersPie = (30 * 24) - (q1Pie + q2Pie + q3Pie + q4Pie + pt);;
        }
        Log.d("Personal Time", Float.toString(pt));
        Log.i("othersPie value", Float.toString(othersPie));
    }

    private void logMillisecondToDate(String title, Long ms) {
        Calendar day = new GregorianCalendar();
        day.setTimeInMillis(ms);
        Log.d(title,
                Integer.toString(day.get(Calendar.MONTH) + 1) +
                        "/" +
                        Integer.toString(day.get(Calendar.DAY_OF_MONTH)) +
                        " " +
                        Integer.toString(day.get(Calendar.HOUR_OF_DAY)) +
                        ":" +
                        Integer.toString(day.get(Calendar.MINUTE)) +
                        ":" +
                        Integer.toString(day.get(Calendar.SECOND)));
    }

    private LineData generateDataLine(int cnt) {
        int numEntries;
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        ArrayList<Entry> e2 = new ArrayList<Entry>();
        ArrayList<Entry> e3 = new ArrayList<Entry>();
        ArrayList<Entry> e4 = new ArrayList<Entry>();
        ArrayList<Entry> e5 = new ArrayList<Entry>();
        ArrayList<TaskItem> q1 = new ArrayList<TaskItem>();
        ArrayList<TaskItem> q2 = new ArrayList<TaskItem>();
        ArrayList<TaskItem> q3 = new ArrayList<TaskItem>();
        ArrayList<TaskItem> q4 = new ArrayList<TaskItem>();
        // sort all tasks into quadrants
        ArrayList<ArrayList<TaskItem>> quadrants= sortGoalListToQuadrants();
        // set data
        q1 = quadrants.get(0);
        q2 = quadrants.get(1);
        q3 = quadrants.get(2);
        q4 = quadrants.get(3);

        Log.d("q1 size", Integer.toString(q1.size()));
        Log.d("q2 size", Integer.toString(q2.size()));
        Log.d("q3 size", Integer.toString(q3.size()));
        Log.d("q4 size", Integer.toString(q4.size()));

        if (showInWeek)
            numEntries = 7;
        else
            numEntries = 30;

        Calendar cal = new GregorianCalendar();
        // set date back numEntries days and start from there to now
        cal.add(Calendar.DATE, -numEntries);
        for (int i = 0; i < numEntries; i++) {
            float e1Size = 0;
            float e2Size = 0;
            float e3Size = 0;
            float e4Size = 0;
            float othersSize = 0;
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            Log.d("Iteration " + Integer.toString(i), "-----------------------------");
            Log.d("Date", Integer.toString(year) + "/" +
                    Integer.toString(month + 1) + "/" +
                    Integer.toString(day));

            Calendar thisDay = new GregorianCalendar();
            thisDay.set(year, month, day, 0, 0, 0);
            long dayAt0000 = thisDay.getTimeInMillis(); // in millisecond
            thisDay.set(year, month, day, 23, 59, 59);
            long dayAt2359 = thisDay.getTimeInMillis(); // in millisecond
            logMillisecondToDate("0000", dayAt0000);
            logMillisecondToDate("2359", dayAt2359);

            // check if task is within the day's range,
            // if so, add to according size in hours.
            for (TaskItem ti : q1) {
                // Fixme: timeCompeted should not be deadline.
                long timeCompleted = ti.getDeadline();
//                logMillisecondToDate(timeCompleted);
                if (timeCompleted >= dayAt0000 && timeCompleted <= dayAt2359) {
                    Log.d("add to e1", ti.getTitle());
                    e1Size = e1Size + (float) ti.getDuration() / 100 / 60 / 60;
                }
            }
            for (TaskItem ti : q2) {
                // Fixme: timeCompeted should not be deadline.
                long timeCompleted = ti.getDeadline();
//                logMillisecondToDate(timeCompleted);
                if (timeCompleted >= dayAt0000 && timeCompleted <= dayAt2359) {
                    Log.d("add to e2", ti.getTitle());
                    e2Size = e2Size + (float) ti.getDuration() / 100 / 60 / 60;
                }
            }
            for (TaskItem ti : q3) {
                // Fixme: timeCompeted should not be deadline.
                long timeCompleted = ti.getDeadline();
//                logMillisecondToDate(timeCompleted);
                if (timeCompleted >= dayAt0000 && timeCompleted <= dayAt2359) {
                    Log.d("add to e3", ti.getTitle());
                    e3Size = e3Size + (float) ti.getDuration() / 100 / 60 / 60;
                }
            }
            for (TaskItem ti : q4) {
                // Fixme: timeCompeted should not be deadline.
                long timeCompleted = ti.getDeadline();
//                logMillisecondToDate(timeCompleted);
                if (timeCompleted >= dayAt0000 && timeCompleted <= dayAt2359) {
                    Log.d("add to e4", ti.getTitle());
                    e4Size = e4Size + (float) ti.getDuration() / 100 / 60 / 60;
                }
            }
            // calc otherSize value (hours in day - (e1Size + ... e4Size + personalTime)
            othersSize = 24 -
                    (e1Size + e2Size + e3Size + e4Size + personalTime);

            Log.d("e1Size - " + Integer.toString(i), Float.toString(e1Size));
            Log.d("e2Size - " + Integer.toString(i), Float.toString(e2Size));
            Log.d("e3Size - " + Integer.toString(i), Float.toString(e3Size));
            Log.d("e4Size - " + Integer.toString(i), Float.toString(e4Size));
            Log.d("othersSize", Float.toString(othersSize));

            e1.add(new Entry(i, e1Size));
            e2.add(new Entry(i, e2Size));
            e3.add(new Entry(i, e3Size));
            e4.add(new Entry(i, e4Size));
            e5.add(new Entry(i, othersSize));

            // subtract date by 1 so it will get the next (previous) day's
            // 0000 and 1159 in milliseconds.
            cal.add(Calendar.DATE, 1);
        }

        // for randomly generated data (demo).
//        for (int i = 0; i < numEntries; i++) {
//            int n = (int) (Math.random() * 65) + 40;
//            e1.add(new Entry(i, n));
//            e2.add(new Entry(i, n - 10));
//            e3.add(new Entry(i, n - 20));
//            e4.add(new Entry(i, n - 30));
//            e5.add(new Entry(i, n - 40));
//        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]); // green
        d1.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d1.setDrawValues(false);

        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[1]); // yellow
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        d2.setDrawValues(false);

        LineDataSet d3 = new LineDataSet(e3, "New DataSet " + cnt + ", (3)");
        d3.setLineWidth(2.5f);
        d3.setCircleRadius(4.5f);
        d3.setHighLightColor(Color.rgb(244, 117, 117));
        d3.setColor(ColorTemplate.VORDIPLOM_COLORS[2]); // orange
        d3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d3.setDrawValues(false);

        LineDataSet d4 = new LineDataSet(e4, "New DataSet " + cnt + ", (4)");
        d4.setLineWidth(2.5f);
        d4.setCircleRadius(4.5f);
        d4.setHighLightColor(Color.rgb(244, 117, 117));
        d4.setColor(ColorTemplate.VORDIPLOM_COLORS[3]); // blue
        d4.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        d4.setDrawValues(false);

        LineDataSet d5 = new LineDataSet(e5, "New DataSet " + cnt + ", (5)");
        d5.setLineWidth(2.5f);
        d5.setCircleRadius(4.5f);
        d5.setHighLightColor(Color.rgb(244, 117, 117));
        d5.setColor(ColorTemplate.VORDIPLOM_COLORS[4]); // red
        d5.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        d5.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);
        sets.add(d3);
        sets.add(d4);
        sets.add(d5);

        LineData cd = new LineData(sets);
        return cd;
    }

    private PieData generateDataPie(int cnt) {

        calcQuadrantsForPie();

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        Log.d("goalList size", Integer.toString(goalList.size()) + " ");

        entries.add(new PieEntry((float) q1Pie, "Quadrant 1"));
        entries.add(new PieEntry((float) q2Pie, "Quadrant 2"));
        entries.add(new PieEntry((float) q3Pie, "Quadrant 3"));
        entries.add(new PieEntry((float) q4Pie, "Quadrant 4"));
        entries.add(new PieEntry((float) othersPie, "Others"));


        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);

        return cd;
    }
}
