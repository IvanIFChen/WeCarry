package space.wecarry.wecarryapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.activity.RoleGoalActivity;
import space.wecarry.wecarryapp.adapter.RoleGoalAdapter;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_ROLE_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_GOAL_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_ROLE_LIST;

/**
 * Created by Ivan IF Chen on 8/2/2016.
 */
public class RoleGoalFragment extends Fragment {

    private ListView listView;
    private ArrayList mList;
    private RoleGoalAdapter adapter;
    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private Cursor cursorRole= null ,cursorGoal = null;

    public RoleGoalFragment() { }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_role_goal, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_role_goal));
        listView = (ListView) rootView.findViewById(R.id.listView);

        getRoleGoalData();


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

    private void getRoleGoalData() {
        mList = new ArrayList<>();
        // TODO: Get data from DB
        // Open db
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        cursorRole = db.rawQuery("SELECT * FROM " + TABLE_NAME_ROLE_LIST, null);
        int roleDataRow = cursorRole.getCount();
        if(roleDataRow != 0) {
            cursorRole.moveToFirst();
            RoleItem roleItem = new RoleItem();
            GoalItem goalItem = new GoalItem();
            for(int i=0; i<roleDataRow; i++) {
                // Get role
                roleItem.setTitle(cursorRole.getString(2));
                roleItem.setDeadline(cursorRole.getLong(3));
                roleItem.setDuration(cursorRole.getLong(4));
                cursorGoal = db.rawQuery("SELECT * FROM " + TABLE_NAME_GOAL_LIST +
                        " WHERE "+GOAL_ROLE_ID+" = "+cursorRole.getString(0), null);    //  TODO: Now our role id is at column 1, but (in local) SQLite we use column 0 to be the key.
                // Get goal
                cursorGoal.moveToFirst();
                for(int j=0; j<cursorGoal.getCount(); j++) {
                    goalItem.setTitle(cursorGoal.getString(2));
                    goalItem.setDeadline(cursorGoal.getLong(3));
                    goalItem.setDuration(cursorGoal.getLong(4));
                    goalItem.setImportance(Boolean.valueOf(cursorGoal.getString(5)));   // SQLite can't use boolean type?
                    goalItem.setUrgency(Boolean.valueOf(cursorGoal.getString(6)));
                    roleItem.addGoalItem(goalItem);
                    cursorGoal.moveToNext();
                }
                mList.add(roleItem);
                cursorRole.moveToNext();
            }
        }else {
            // There are no role data in DB
            // TODO:  show a view for empty
        }

//        // Testing (Sample)
//        RoleItem roleItem = new RoleItem();
//        GoalItem goalItem = new GoalItem();
//        roleItem.setTitle("Student");
//        goalItem.setTitle("Read a book");
//        goalItem.setDeadline(Calendar.getInstance().getTimeInMillis()+2*60*60*1000);
//        goalItem.setDuration(2*60*60*1000);
//        goalItem.setImportance(true);
//        goalItem.setUrgency(true);
//        roleItem.addGoalItem(goalItem);
//        GoalItem goalItem2 = new GoalItem();
//        goalItem2.setTitle("Be the top one");
//        roleItem.addGoalItem(goalItem2);
//        mList.add(roleItem);
//
//        roleItem = new RoleItem();
//        roleItem.setTitle("Programmer");
//        mList.add(roleItem);
//
//        roleItem = new RoleItem();
//        goalItem = new GoalItem();
//        roleItem.setTitle("TA");
//        goalItem.setTitle("Teach a student");
//        roleItem.addGoalItem(goalItem);
//        goalItem = new GoalItem();
//        goalItem.setTitle("Teach a student");
//        roleItem.addGoalItem(goalItem);
//        goalItem = new GoalItem();
//        goalItem.setTitle("Teach a studenttttttttt");
//        roleItem.addGoalItem(goalItem);
//        goalItem = new GoalItem();
//        goalItem.setTitle("Teach a student");
//        roleItem.addGoalItem(goalItem);
//        mList.add(roleItem);

    }

}
