package space.wecarry.wecarryapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import space.wecarry.wecarryapp.activity.PlanActivity;
import space.wecarry.wecarryapp.adapter.CustomExpandableListAdapter;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.TaskItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

import static space.wecarry.wecarryapp.sqlite.DBConstants.RESOURCE_GOAL_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.RESOURCE_TASK_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_GOAL_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_RESOURCE_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_TASK_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_GOAL_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_ID;

/**
 * Created by Ivan IF Chen on 8/2/2016.
 */
public class PlanFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    private ArrayList<GoalItem> goalList;
    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private Cursor cursorGoal = null ,cursorTask = null;

    public PlanFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_plan));
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);

        setHasOptionsMenu(true);

        // set empty view
        View emptyView = rootView.findViewById(R.id.view_empty);
        expandableListView.setEmptyView(emptyView);

        updateListView();

        expandList();

        // group click
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // only edit when the group size is 0 (this goal has no tasks).
                if (goalList.get(groupPosition).getTaskList().size() == 0)
                    startEditGoalActivity(groupPosition);

                return false;
            }
        });

        // child click
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(getActivity(), Integer.toString(groupPosition)
//                        + " - " + Integer.toString(childPosition), Toast.LENGTH_SHORT).show();

                startEditGoalActivity(groupPosition);

                return false;
            }
        });

        // Long Click-------------------------------------------------------------------------------
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                long packedPosition = expandableListView.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);


                /*  if group item long clicked */
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    // delete the group (goal)
                    GoalItem goalItem = goalList.get(groupPosition);
                    deleteGoalDialog(goalItem.getTitle(), goalItem.getGoalId(), view);
                }

                /*  if child item long clicked */
                else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    // delete the child (task)
                    GoalItem goalItem = goalList.get(groupPosition);
                    TaskItem taskItem = goalItem.getTaskList().get(childPosition);
                    deleteTaskDialog(taskItem.getTitle(), taskItem.getTaskId(), view);
                }

                // return true no matter what. Because it will cause conflict with the other click
                // listener (long click is also a click, if false will trigger both listeners).
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.menu_expandable_listview,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_expand_all :
                expandList();
                break;
            case R.id.action_collapse_all :
                collapseList();
                break;

        }
        return true;
    }

    private void startEditGoalActivity(int goalPosition) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), PlanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("goalUserSelected", goalPosition);
        bundle.putSerializable("goalItem", goalList.get(goalPosition));
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    private void updateListView() {
        // get roleList data from local db.
        getGoalTaskData();

        expandableListDetail = ListDataConverter.convertGoalListData(goalList);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());

        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void expandList() {
        // Expand all groups.
        int count = expandableListAdapter.getGroupCount();
        for (int position = 0; position < count; position++)
            expandableListView.expandGroup(position);
    }

    private void collapseList() {
        // Expand all groups.
        int count = expandableListAdapter.getGroupCount();
        for (int position = 0; position < count; position++)
            expandableListView.collapseGroup(position);
    }

    private void getGoalTaskData() {
        goalList = new ArrayList<>();
        // Open db
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        cursorGoal = db.rawQuery("SELECT * FROM " + TABLE_NAME_GOAL_LIST, null);
        int goalDataRow = cursorGoal.getCount();
        if(goalDataRow != 0) {
            cursorGoal.moveToFirst();
            for(int i=0; i<goalDataRow; i++) {
                // Get goal
                GoalItem goalItem = new GoalItem();
                goalItem.setGoalId(cursorGoal.getInt(0));   //  TODO: Now our goal id is at column 1, but (in local) SQLite we use column 0 to be the key.
                goalItem.setTitle(cursorGoal.getString(2));
                goalItem.setDeadline(cursorGoal.getLong(3));
                goalItem.setDuration(cursorGoal.getLong(4));
                goalItem.setImportant(Boolean.valueOf(cursorGoal.getString(5)));
                goalItem.setUrgent(Boolean.valueOf(cursorGoal.getString(6)));
                goalItem.setRoleId(cursorGoal.getInt(7));
                cursorTask = db.rawQuery("SELECT * FROM " + TABLE_NAME_TASK_LIST +
                                " WHERE " + TASK_GOAL_ID + " = " + goalItem.getGoalId(), null);
                // Get task
                int taskDataRow = cursorTask.getCount();
                if(taskDataRow != 0) {
                    cursorTask.moveToFirst();
                    for(int j=0; j<taskDataRow; j++) {
                        TaskItem taskItem = new TaskItem();
                        taskItem.setTaskId(cursorTask.getInt(0));
                        taskItem.setTitle(cursorTask.getString(2));
                        taskItem.setMilestone(Boolean.parseBoolean(cursorTask.getString(3)));
                        taskItem.setEarliestStartTime(cursorTask.getInt(4));
                        taskItem.setLatestStartTime(cursorTask.getInt(5));
                        taskItem.setEarliestStartTime(cursorTask.getInt(6));
                        taskItem.setEarliestEndTime(cursorTask.getInt(7));
                        taskItem.setDeadline(cursorTask.getLong(8));
                        taskItem.setDuration(cursorTask.getLong(9));
                        taskItem.setGoalId(cursorTask.getInt(12));
                        taskItem.setRoleId(cursorTask.getInt(13));
                        // TODO: Get preprocessList
                        // TODO: Get resourcesList
                        goalItem.addTaskItem(taskItem);
                        cursorTask.moveToNext();
                    }
                }
                goalList.add(goalItem);
                cursorGoal.moveToNext();
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

    private void deleteGoalDialog(String goalTitle, final int goalId, final View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("提示");
        dialog.setMessage("您要刪除"+goalTitle+"這個目標？");
        dialog.setPositiveButton("刪除",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                db.delete(TABLE_NAME_GOAL_LIST,"_ID=" + String.valueOf(goalId), null);
                db.delete(TABLE_NAME_TASK_LIST, TASK_GOAL_ID + "=" + String.valueOf(goalId), null);
                db.delete(TABLE_NAME_RESOURCE_LIST, RESOURCE_GOAL_ID + "=" + String.valueOf(goalId), null);
                // TODO: Delete Event ??
                Snackbar.make(view, "已刪除", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                updateListView();
                expandList();
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

    private void deleteTaskDialog(String taskTitle, final int taskId, final View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("提示");
        dialog.setMessage("您要刪除"+taskTitle+"這個目標？");
        dialog.setPositiveButton("刪除",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                db.delete(TABLE_NAME_TASK_LIST, "_ID=" + String.valueOf(taskId), null);
                db.delete(TABLE_NAME_RESOURCE_LIST, RESOURCE_TASK_ID + "=" + String.valueOf(taskId), null);
                // TODO: Delete Event ??
                Snackbar.make(view, "已刪除", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                updateListView();
                expandList();
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
