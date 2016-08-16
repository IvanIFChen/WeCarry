package space.wecarry.wecarryapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
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
import space.wecarry.wecarryapp.activity.DelegateActivty;
import space.wecarry.wecarryapp.adapter.DelegateAdapter;
import space.wecarry.wecarryapp.item.TaskItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_TASK_LIST;

/**
 * Created by Ivan IF Chen on 8/2/2016.
 */
public class DelegateFragment extends Fragment {

    private ListView listView;
    private ArrayList<TaskItem> taskList;
    private DelegateAdapter adapter;
    public DelegateFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delegate, container, false);
        getActivity().setTitle(getString(R.string.navigation_drawer_delegate));
        listView = (ListView) rootView.findViewById(R.id.listView);

        // get taskList data from db.
        getTaskData();

        // setting up adapter
        adapter = new DelegateAdapter(getActivity(), taskList);
        listView.setAdapter(adapter);
        View emptyView = rootView.findViewById(R.id.view_empty);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), DelegateActivty.class);
                Bundle bundle = new Bundle();
                bundle.putInt("taskIDSelected", position);
                bundle.putSerializable("taskItem", taskList.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        }));
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TaskItem ti = taskList.get(position);
                deleteTaskDialog(ti.getTitle(), ti.getTaskId(), view);
                return true;
            }
        });
        return rootView;
    }

    private void getTaskData() {
        DBHelper dbHelper = new DBHelper(getActivity());
        taskList = dbHelper.getAllTasks();
        dbHelper.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            getTaskData();
            adapter = new DelegateAdapter(getActivity(), taskList);
            listView.setAdapter(adapter);
            Toast.makeText(getActivity(),"Update",Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteTaskDialog(String taskTitle, final int taskID, final View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("提示");
        dialog.setMessage("您要刪除" + taskTitle + "這個目標？");
        dialog.setPositiveButton("刪除",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                DBHelper dbHelper = new DBHelper(getActivity());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                db.delete(TABLE_NAME_TASK_LIST,"_ID=" + String.valueOf(taskID), null);
                Snackbar.make(view, "已刪除", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                getTaskData();
                adapter = new DelegateAdapter(getActivity(), taskList);
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
