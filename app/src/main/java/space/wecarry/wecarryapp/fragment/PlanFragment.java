package space.wecarry.wecarryapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.activity.PlanActivity;
import space.wecarry.wecarryapp.adapter.PlanAdapter;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.TaskItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_ROLE_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_GOAL_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_TASK_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_GOAL_ID;

/**
 * Created by Ivan IF Chen on 8/2/2016.
 */
public class PlanFragment extends Fragment {

    private ListView listView;
    private ArrayList mList;
    private PlanAdapter adapter;
    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private Cursor cursorGoal = null ,cursorTask = null;

    public PlanFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_plan));
        listView = (ListView) rootView.findViewById(R.id.listView);

        // Get date and print-----------------------------------------------------------------------
        getGoalTaskData();
        adapter = new PlanAdapter(getActivity(), mList);
        listView.setAdapter(adapter);
        View emptyView = rootView.findViewById(R.id.view_empty);
        listView.setEmptyView(emptyView);

        // Click------------------------------------------------------------------------------------
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PlanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("goalUserSelected", position);
                bundle.putSerializable("goalItem", ((GoalItem)mList.get(position)));
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        // Long Click-------------------------------------------------------------------------------
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                GoalItem goalItem = (GoalItem) mList.get(position);
                deleteGoalDialog(goalItem.getTitle(), goalItem.getId(), view);
                return true;
            }
        });

        return rootView;
    }

    private void getGoalTaskData() {
        mList = new ArrayList<>();
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
                goalItem.setId(cursorGoal.getInt(0));   //  TODO: Now our goal id is at column 1, but (in local) SQLite we use column 0 to be the key.
                goalItem.setTitle(cursorGoal.getString(2));
                goalItem.setDeadline(cursorGoal.getLong(3));
                goalItem.setDuration(cursorGoal.getLong(4));
                goalItem.setImportant(Boolean.valueOf(cursorGoal.getString(5)));
                goalItem.setUrgent(Boolean.valueOf(cursorGoal.getString(6)));
                cursorTask = db.rawQuery("SELECT * FROM " + TABLE_NAME_TASK_LIST +
                                " WHERE " + TASK_GOAL_ID + " = " + goalItem.getId(), null);
                // Get task
                int taskDataRow = cursorTask.getCount();
                if(taskDataRow != 0) {
                    cursorTask.moveToFirst();
                    for(int j=0; j<taskDataRow; j++) {
                        TaskItem taskItem = new TaskItem();
                        taskItem.setId(cursorTask.getInt(0));
                        taskItem.setTitle(cursorTask.getString(2));
                        taskItem.setEarliestStartTime(cursorTask.getInt(3));
                        taskItem.setLatestStartTime(cursorTask.getInt(4));
                        taskItem.setEarliestStartTime(cursorTask.getInt(5));
                        taskItem.setEarliestEndTime(cursorTask.getInt(6));
                        taskItem.setDeadline(cursorTask.getLong(7));
                        taskItem.setDuration(cursorTask.getLong(8));
                        // TODO: Get preprocessList
                        // TODO: Get resourcesList
                        goalItem.addTaskItem(taskItem);
                        cursorTask.moveToNext();
                    }
                }
                mList.add(goalItem);
                cursorGoal.moveToNext();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            getGoalTaskData();
            adapter = new PlanAdapter(getActivity(), mList);
            listView.setAdapter(adapter);
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
                Snackbar.make(view, "已刪除", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                getGoalTaskData();
                adapter = new PlanAdapter(getActivity(), mList);
                listView.setAdapter(adapter);
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
