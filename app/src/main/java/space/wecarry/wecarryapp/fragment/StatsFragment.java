package space.wecarry.wecarryapp.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    private long q1Size, q2Size, q3Size, q4Size, otherSize;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Enter", "stats");
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_stats));

        Utils.init(getActivity());

        ListView lv = (ListView) rootView.findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        list.add(new LineChartItem(generateDataLine(5), getActivity()));
        list.add(new PieChartItem(generateDataPie(1), getActivity()));

        ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
        lv.setAdapter(cda);

        setHasOptionsMenu(true);

        // Insert test data to db
        Log.d("Start test data", "=============================================");
        getDataFromDB();
        Log.d("End test data", "===============================================");

        return rootView;
    }

    private long calculateTotalDurationFromGoal(GoalItem gi) {
        long sum = 0;
        ArrayList<TaskItem> tasks = gi.getTaskList();
        for (TaskItem ti : tasks) {
            sum = sum + ti.getDuration();
        }
        return sum;
    }

    private int checkQuadrant(GoalItem gi) {
        if (gi.isImportant() && gi.isUrgent()) {
            return 1;
        }
        else if (gi.isImportant() && !gi.isUrgent()) {
            return 2;
        }
        else if (!gi.isImportant() && gi.isUrgent()) {
            return 3;
        }
        else {
            return 4;
        }
    }

    private void getDataFromDB() {
        // TODO: load data from db
        // sample data
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 10);
        long g1Deadline = cal.getTimeInMillis();
        Log.d("g1Deadline", Long.toString(g1Deadline));
        cal.add(Calendar.DATE, 10);
        long g2Deadline = cal.getTimeInMillis();
        Log.d("g2Deadline", Long.toString(g2Deadline));
        cal.add(Calendar.DATE, 10);
        long g3Deadline = cal.getTimeInMillis();
        Log.d("g3Deadline", Long.toString(g3Deadline));

        roleList = new ArrayList<RoleItem>();
        goalList = new ArrayList<GoalItem>();
        taskList = new ArrayList<TaskItem>();

        ArrayList<TaskItem> g1Tasks = new ArrayList<TaskItem>();
        ArrayList<TaskItem> g2Tasks = new ArrayList<TaskItem>();
        ArrayList<TaskItem> g3Tasks = new ArrayList<TaskItem>();
        ArrayList<GoalItem> r1Goals = new ArrayList<GoalItem>();
        ArrayList<GoalItem> r2Goals = new ArrayList<GoalItem>();

        TaskItem t1 = new TaskItem("task1", 10);
        TaskItem t2 = new TaskItem("task2", 20);
        TaskItem t3 = new TaskItem("task3", 30);
        TaskItem t4 = new TaskItem("task4", 40);
        g1Tasks.add(t1);
        g1Tasks.add(t2);
        g2Tasks.add(t3);
        g3Tasks.add(t4);
        GoalItem g1 = new GoalItem("goal1", g1Deadline, true, true, g1Tasks);
        GoalItem g2 = new GoalItem("goal2", g2Deadline, true, true, g2Tasks);
        GoalItem g3 = new GoalItem("goal3", g3Deadline, false, false, g3Tasks);
        r1Goals.add(g1);
        r1Goals.add(g2);
        r2Goals.add(g3);
        RoleItem r1 = new RoleItem("role1", r1Goals);
        RoleItem r2 = new RoleItem("role2", r2Goals);

        // adding sample data to lists.
        roleList.add(r1);
        roleList.add(r2);
        goalList.add(g1);
        goalList.add(g2);
        goalList.add(g3);
        taskList.add(t1);
        taskList.add(t2);
        taskList.add(t3);
        taskList.add(t4);

        // output: 1 1 4
        Log.d("Test: checkQuadrant", Long.toString(checkQuadrant(g1)));
        Log.d("Test: checkQuadrant", Long.toString(checkQuadrant(g2)));
        Log.d("Test: checkQuadrant", Long.toString(checkQuadrant(g3)));
        // output: 30 30 40
        Log.d("Test: totalDuration", Long.toString(calculateTotalDurationFromGoal(g1)));
        Log.d("Test: totalDuration", Long.toString(calculateTotalDurationFromGoal(g2)));
        Log.d("Test: totalDuration", Long.toString(calculateTotalDurationFromGoal(g3)));

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

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // TODO: Insert data here.
//        for (int i = 0; i < 5; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quadrant " + (i+1)));
//        }
        for (int i = 0; i < goalList.size(); i++) {
            ArrayList<GoalItem> q1;
            ArrayList<GoalItem> q2;
            ArrayList<GoalItem> q3;
            ArrayList<GoalItem> q4;
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);

        return cd;
    }
}
