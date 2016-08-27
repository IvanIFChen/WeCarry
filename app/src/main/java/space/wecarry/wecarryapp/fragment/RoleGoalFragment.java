package space.wecarry.wecarryapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import space.wecarry.wecarryapp.ListDataConverter;
import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.activity.RoleGoalActivity;
import space.wecarry.wecarryapp.adapter.CustomExpandableListAdapter;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_ROLE_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.RESOURCE_ROLE_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_GOAL_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_RESOURCE_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_ROLE_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_TASK_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_ROLE_ID;

/**
 * Created by Ivan IF Chen on 8/2/2016.
 */
public class RoleGoalFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    private ArrayList roleList;
    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private Cursor cursorRole= null ,cursorGoal = null;

    public RoleGoalFragment() { }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_role_goal, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_role_goal));
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);

        // set empty view
        View emptyView = rootView.findViewById(R.id.view_empty);
        expandableListView.setEmptyView(emptyView);

        updateListView();

        expandList();

        // Click ----------------------------------------------------------------------------------
//        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//            @Override
//            public void onGroupExpand(int groupPosition) { }
//        });
//        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//            @Override
//            public void onGroupCollapse(int groupPosition) { }
//        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RoleGoalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("roleUserSelected", groupPosition);
                bundle.putSerializable("roleItem", ((RoleItem) roleList.get(groupPosition)));
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                return false;
            }
        });

        // Click-----------------------------------------------------------------------------------
//        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), RoleGoalActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("roleUserSelected", position);
//                bundle.putSerializable("roleItem", ((RoleItem) roleList.get(position)));
//                intent.putExtras(bundle);
//                startActivityForResult(intent, 0);
//            }
//        });
        // Long Click-------------------------------------------------------------------------------
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                RoleItem roleItem = (RoleItem) roleList.get(position);
                deleteRoleDialog(roleItem.getTitle(), roleItem.getId(), view);
                return true;
            }
        });

        // FAB--------------------------------------------------------------------------------------
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RoleGoalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("roleUserSelected", -1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });

        return rootView;
    }

    private void updateListView() {
        // get roleList data from local db.
        getRoleGoalData();

        expandableListDetail = ListDataConverter.convertRoleListData(roleList);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void expandList() {
        // Expand all groups.
        int count = expandableListAdapter.getGroupCount();
        for (int position = 1; position <= count; position++)
            expandableListView.expandGroup(position - 1);
    }

    private void getRoleGoalData() {
        roleList = new ArrayList<>();
        // Open db
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        cursorRole = db.rawQuery("SELECT * FROM " + TABLE_NAME_ROLE_LIST, null);
        int roleDataRow = cursorRole.getCount();
        if(roleDataRow != 0) {
            cursorRole.moveToFirst();
            for(int i=0; i<roleDataRow; i++) {
                // Get role
                RoleItem roleItem = new RoleItem();
                roleItem.setId(cursorRole.getInt(0));   //  TODO: Now our role id is at column 1, but (in local) SQLite we use column 0 to be the key.
                roleItem.setTitle(cursorRole.getString(2));
                roleItem.setDeadline(cursorRole.getLong(3));
                roleItem.setDuration(cursorRole.getLong(4));
                cursorGoal = db.rawQuery("SELECT * FROM " + TABLE_NAME_GOAL_LIST +
                        " WHERE " + GOAL_ROLE_ID + " = " + roleItem.getId()
                        , null);
                // Get goal
                int goalDataRow = cursorGoal.getCount();
                if(goalDataRow != 0) {
                    cursorGoal.moveToFirst();
                    for(int j=0; j<goalDataRow; j++) {
                        GoalItem goalItem = new GoalItem();
                        goalItem.setGoalId(cursorGoal.getInt(0));
                        goalItem.setTitle(cursorGoal.getString(2));
                        goalItem.setDeadline(cursorGoal.getLong(3));
                        goalItem.setDuration(cursorGoal.getLong(4));
                        goalItem.setImportant(Boolean.valueOf(cursorGoal.getString(5)));
                        goalItem.setUrgent(Boolean.valueOf(cursorGoal.getString(6)));
                        goalItem.setRoleId(cursorGoal.getInt(7));
                        roleItem.addGoalItem(goalItem);
                        cursorGoal.moveToNext();
                    }
                }
                roleList.add(roleItem);
                cursorRole.moveToNext();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            updateListView();
            expandList();

            Toast.makeText(getActivity(),"Update",Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRoleDialog(String roleTitle, final int roleId, final View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("提示");
        dialog.setMessage("您要刪除"+roleTitle+"這個角色？");
        dialog.setPositiveButton("刪除",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                db.delete(TABLE_NAME_ROLE_LIST,"_ID=" + String.valueOf(roleId), null);
                db.delete(TABLE_NAME_GOAL_LIST, GOAL_ROLE_ID + "=" + String.valueOf(roleId), null);
                db.delete(TABLE_NAME_TASK_LIST, TASK_ROLE_ID + "=" + String.valueOf(roleId), null);
                db.delete(TABLE_NAME_RESOURCE_LIST, RESOURCE_ROLE_ID + "=" + String.valueOf(roleId), null);
                // TODO: Delete Event ??
                Snackbar.make(view, "已刪除", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                updateListView();
            }
        });
        dialog.setNeutralButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            // Cancel
            }
        });
        dialog.show();
    }
}
