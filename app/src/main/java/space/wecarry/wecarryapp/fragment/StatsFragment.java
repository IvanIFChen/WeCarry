package space.wecarry.wecarryapp.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.UserConstants;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;
import space.wecarry.wecarryapp.item.TaskItem;
import space.wecarry.wecarryapp.listviewitems.ChartItem;
import space.wecarry.wecarryapp.listviewitems.LineChartItem;
import space.wecarry.wecarryapp.listviewitems.PieChartItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;
import space.wecarry.wecarryapp.UserConstants.*;

public class StatsFragment extends Fragment {
    private DBHelper dbHelper;
    private ArrayList<RoleItem> roleList;
    private ArrayList<GoalItem> goalList;
    private ArrayList<TaskItem> taskList;
    private boolean showInWeek = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Enter", "stats");
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_stats));

        Utils.init(getActivity());

        // Insert test data to db
        Log.d("Start test data", "=============================================");
        getDataFromDB();
        Log.d("End test data", "===============================================");

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
        // TODO: load data from db
        // sample data
        // re-initialize
        roleList = new ArrayList<RoleItem>();
        goalList = new ArrayList<GoalItem>();
        taskList = new ArrayList<TaskItem>();

        ArrayList<TaskItem> g1Tasks = new ArrayList<TaskItem>();
        ArrayList<TaskItem> g2Tasks = new ArrayList<TaskItem>();
        ArrayList<TaskItem> g3Tasks = new ArrayList<TaskItem>();
        ArrayList<TaskItem> g4Tasks = new ArrayList<TaskItem>();
        ArrayList<TaskItem> g5Tasks = new ArrayList<TaskItem>();
        ArrayList<GoalItem> r1Goals = new ArrayList<GoalItem>();
        ArrayList<GoalItem> r2Goals = new ArrayList<GoalItem>();

        // random value = (max ~1.5hr, min ~.5hr)
        int nameCount = 1;
        for (int i = 1; i <= 10; i++) {
            TaskItem task;
            task = new TaskItem("task" + nameCount, (long) (Math.random() * 70 + 30));
//            Log.e("task", task.toString());
            g1Tasks.add(task);
            taskList.add(task);
            nameCount = nameCount + 1;
            task = new TaskItem("task" + nameCount, (long) (Math.random() * 70 + 30));
//            Log.e("task", task.toString());
            g2Tasks.add(task);
            taskList.add(task);
            nameCount = nameCount + 1;
            task = new TaskItem("task" + nameCount, (long) (Math.random() * 70 + 30));
//            Log.e("task", task.toString());
            g3Tasks.add(task);
            taskList.add(task);
            nameCount = nameCount + 1;
            task = new TaskItem("task" + nameCount, (long) (Math.random() * 70 + 30));
//            Log.e("task", task.toString());
            g4Tasks.add(task);
            taskList.add(task);
            nameCount = nameCount + 1;
            task = new TaskItem("task" + nameCount, (long) (Math.random() * 70 + 30));
//            Log.e("task", task.toString());
            g5Tasks.add(task);
            taskList.add(task);
        }

        GoalItem g1 = new GoalItem("goal1", 0, true, true, g1Tasks); // will add deadline later
        GoalItem g2 = new GoalItem("goal2", 0, true, false, g2Tasks);
        GoalItem g3 = new GoalItem("goal3", 0, false, true, g3Tasks);
        GoalItem g4 = new GoalItem("goal4", 0, false, false, g4Tasks);
        GoalItem g5 = new GoalItem("goal5", 0, true, false, g5Tasks);
        r1Goals.add(g1);
        r1Goals.add(g2);
        r1Goals.add(g3);
        r2Goals.add(g4);
        r2Goals.add(g5);
        RoleItem r1 = new RoleItem("role1", r1Goals);
        RoleItem r2 = new RoleItem("role2", r2Goals);

        // adding sample data to lists.
        roleList.add(r1);
        roleList.add(r2);
        goalList.add(g1);
        goalList.add(g2);
        goalList.add(g3);
        goalList.add(g4);
        goalList.add(g5);

        // Adding deadline for goals and tasks
        for (GoalItem gi : goalList) {
            Calendar cal = new GregorianCalendar();
            gi.setDeadline(cal.getTimeInMillis());
//            Log.d("Today", "   " + Long.toString(cal.getTimeInMillis()));
            for (TaskItem ti : gi.getTaskList()) {
                cal.add(cal.DATE, -1);
//                Log.d("Today -1", cal.toString());
                ti.setDeadline(cal.getTimeInMillis());
            }
        }
//        Calendar cal = new GregorianCalendar();
//        cal.set(2016, 7, 10, 15, 0);
//        Log.e("specific time", Long.toString(cal.getTimeInMillis()));
//        cal.set(2016, 7, 10, 19, 32);
//        Log.e("specific time", Long.toString(cal.getTimeInMillis()));


        Log.d("Test: checkQuadrant", Long.toString(g1.checkQuadrant()));
        Log.d("Test: checkQuadrant", Long.toString(g2.checkQuadrant()));
        Log.d("Test: checkQuadrant", Long.toString(g3.checkQuadrant()));
        Log.d("Test: checkQuadrant", Long.toString(g4.checkQuadrant()));
        Log.d("Test: checkQuadrant", Long.toString(g5.checkQuadrant()));

        Log.d("Test: totalDuration", Long.toString(g1.getDuration()));
        Log.d("Test: totalDuration", Long.toString(g2.getDuration()));
        Log.d("Test: totalDuration", Long.toString(g3.getDuration()));
        Log.d("Test: totalDuration", Long.toString(g4.getDuration()));
        Log.d("Test: totalDuration", Long.toString(g5.getDuration()));

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

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine(int cnt) {
        // TODO: Insert data here.

        ArrayList<Entry> e1 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d1.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e2.add(new Entry(i, e1.get(i).getY() - 10));
        }

        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        d2.setDrawValues(false);

        ArrayList<Entry> e3 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e3.add(new Entry(i, e1.get(i).getY() - 20));
        }

        LineDataSet d3 = new LineDataSet(e3, "New DataSet " + cnt + ", (3)");
        d3.setLineWidth(2.5f);
        d3.setCircleRadius(4.5f);
        d3.setHighLightColor(Color.rgb(244, 117, 117));
        d3.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d3.setDrawValues(false);

        ArrayList<Entry> e4 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e4.add(new Entry(i, e1.get(i).getY() - 30));
        }

        LineDataSet d4 = new LineDataSet(e4, "New DataSet " + cnt + ", (4)");
        d4.setLineWidth(2.5f);
        d4.setCircleRadius(4.5f);
        d4.setHighLightColor(Color.rgb(244, 117, 117));
        d4.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        d4.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        d4.setDrawValues(false);

        ArrayList<Entry> e5 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e5.add(new Entry(i, e1.get(i).getY() - 40));
        }

        LineDataSet d5 = new LineDataSet(e5, "New DataSet " + cnt + ", (5)");
        d5.setLineWidth(2.5f);
        d5.setCircleRadius(4.5f);
        d5.setHighLightColor(Color.rgb(244, 117, 117));
        d5.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
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

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(int cnt) {
        float q1 = 0; // in hours
        float q2 = 0;
        float q3 = 0;
        float q4 = 0;
        float other = 0;
        float sleepDuration = UserConstants.sleepDuration;
        float breakfastDuration = UserConstants.breakfastDuration;
        float lunchDuration = UserConstants.lunchDuration;
        float dinnerDuration = UserConstants.dinnerDuration;
        float personalTime = (sleepDuration +
                                    breakfastDuration +
                                    lunchDuration +
                                    dinnerDuration) /60;
        if (showInWeek) {
            personalTime = personalTime * 7;
        } else {
            personalTime = personalTime * 30;
        }
        Log.d("Personal Time", Float.toString(personalTime));

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

//        for (int i = 0; i < 5; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quadrant " + (i+1)));
//        }

        Log.d("goalList size", Integer.toString(goalList.size()) + " ");

        for (GoalItem gi : goalList) {
            switch (gi.checkQuadrant()) {
                case 1:
                    q1 = q1 + ((float) gi.getDuration() / 60);
                    break;
                case 2:
                    q2 = q2 + ((float) gi.getDuration() / 60);
                    break;
                case 3:
                    q3 = q3 + ((float) gi.getDuration() / 60);
                    break;
                case 4:
                    q4 = q4 + ((float) gi.getDuration() / 60);
                    break;
            }
        }
        other = (7 * 24) - (q1 + q2 + q3 + q4 + personalTime);
        Log.d("q1 value", Float.toString(q1));
        Log.d("q2 value", Float.toString(q2));
        Log.d("q3 value", Float.toString(q3));
        Log.d("q4 value", Float.toString(q4));
        Log.d("other value", Float.toString(other));

        entries.add(new PieEntry((float) q1, "Quadrant 1"));
        entries.add(new PieEntry((float) q2, "Quadrant 2"));
        entries.add(new PieEntry((float) q3, "Quadrant 3"));
        entries.add(new PieEntry((float) q4, "Quadrant 4"));
        entries.add(new PieEntry((float) other, "Others"));


        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);

        return cd;
    }
}
