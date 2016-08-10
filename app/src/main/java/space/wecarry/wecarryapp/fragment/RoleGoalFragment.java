package space.wecarry.wecarryapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.activity.RoleGoalActivity;
import space.wecarry.wecarryapp.adapter.RoleGoalAdapter;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;

/**
 * Created by Ivan IF Chen on 8/2/2016.
 */
public class RoleGoalFragment extends Fragment {

    private ListView listView;
    private ArrayList mList;
    private RoleGoalAdapter adapter;

    public RoleGoalFragment() { }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_role_goal, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_role_goal));
        listView = (ListView) rootView.findViewById(R.id.listView);

        getRoleGoal();

        // TODO: Click and do something like edit
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RoleGoalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("roleUserSelected", position);
                bundle.putSerializable("roleItem", ((RoleItem)mList.get(position)));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "#"+Long.toString(id)+" Long Click", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                return false;
            }
        });
        adapter = new RoleGoalAdapter(getActivity(), mList);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RoleGoalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("roleUserSelected", -1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return rootView;
    }

    // Testing
    private void getRoleGoal() {
        mList = new ArrayList<>();
        // TODO: Get data from DB
        // Testing (Sample)
        RoleItem roleItem = new RoleItem();
        GoalItem goalItem = new GoalItem();
        roleItem.setTitle("Student");
        goalItem.setTitle("Read a book");
        goalItem.setDeadline(Calendar.getInstance().getTimeInMillis()+2*60*60*1000);
        goalItem.setDuration(2*60*60*1000);
        goalItem.setImportant(true);
        goalItem.setUrgent(true);
        roleItem.addGoalItem(goalItem);
        GoalItem goalItem2 = new GoalItem();
        goalItem2.setTitle("Be the top one");
        roleItem.addGoalItem(goalItem2);
        mList.add(roleItem);

        roleItem = new RoleItem();
        roleItem.setTitle("Programmer");
        mList.add(roleItem);

        roleItem = new RoleItem();
        goalItem = new GoalItem();
        roleItem.setTitle("TA");
        goalItem.setTitle("Teach a student");
        roleItem.addGoalItem(goalItem);
        goalItem = new GoalItem();
        goalItem.setTitle("Teach a student");
        roleItem.addGoalItem(goalItem);
        goalItem = new GoalItem();
        goalItem.setTitle("Teach a studenttttttttt");
        roleItem.addGoalItem(goalItem);
        goalItem = new GoalItem();
        goalItem.setTitle("Teach a student");
        roleItem.addGoalItem(goalItem);
        mList.add(roleItem);
//
    }

}
