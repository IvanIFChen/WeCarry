package space.wecarry.wecarryapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
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
import space.wecarry.wecarryapp.RoleGoalAdapter;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;

/**
 * Created by Tunabutter on 8/2/2016.
 */
public class RoleGoalFragment extends Fragment {

    private ListView listView;
    private ArrayList mList;
    private RoleGoalAdapter adapter;

    public RoleGoalFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_role, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_role_goal));
        listView = (ListView) rootView.findViewById(R.id.listView);

        getRoleGoal();
        // TODO: Click and do something like edit
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "#"+Long.toString(id)+ " Click",Toast.LENGTH_SHORT).show();
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

        return rootView;
    }

    // Testing
    private void getRoleGoal() {
        mList = new ArrayList<>();
        // TODO: Get data from DB
        // Testing (Sample)
        RoleItem roleItem = new RoleItem();
        GoalItem goalItem = new GoalItem();
        roleItem.setText("Student");
        goalItem.setText("Read a book");
        goalItem.setDeadline(Calendar.getInstance().getTimeInMillis()+2*60*60*1000);
        goalItem.setDuration(2*60*60*1000);
        goalItem.setImportance(true);
        goalItem.setUrgency(true);
        roleItem.addGoalItem(goalItem);
        GoalItem goalItem2 = new GoalItem();
        goalItem2.setText("Be the top one");
        roleItem.addGoalItem(goalItem2);
        mList.add(roleItem);

        roleItem = new RoleItem();
        roleItem.setText("Programmer");
        mList.add(roleItem);

        roleItem = new RoleItem();
        goalItem = new GoalItem();
        roleItem.setText("TA");
        goalItem.setText("Teach a student");
        roleItem.addGoalItem(goalItem);
        goalItem = new GoalItem();
        goalItem.setText("Teach a student");
        roleItem.addGoalItem(goalItem);
        goalItem = new GoalItem();
        goalItem.setText("Teach a studenttttttttt");
        roleItem.addGoalItem(goalItem);
        goalItem = new GoalItem();
        goalItem.setText("Teach a student");
        roleItem.addGoalItem(goalItem);
        mList.add(roleItem);
//
    }

}
