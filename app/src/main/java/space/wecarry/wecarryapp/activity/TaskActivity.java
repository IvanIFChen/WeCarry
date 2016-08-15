package space.wecarry.wecarryapp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.item.TaskItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_DEADLINE;
import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_DURATION;
import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_TITLE;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_GOAL_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_TASK_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_DEADLINE;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_DURATION;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_GOAL_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TASK_TITLE;

public class TaskActivity extends AppCompatActivity {
    private TextView textTask;
    private EditText editTask, editDeadline, editDuration;
    private Button btnConfirm, btnCancel, btnNewList, btnDelete;
    private LinearLayout ll_in_sv;
    private ArrayList<HashMap> objectList;
    private View buttonView;
    private TaskItem mTask, old_mTask;
    private ArrayList<TaskItem> taskList;
    private int mYear, mMonth, mDay;
    private Calendar today = null;
    private int taskItemSelected = -1;
    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        buttonView = LayoutInflater.from(TaskActivity.this).inflate(R.layout.role_goal_object_button, null);
        textTask = (TextView)findViewById(R.id.textTask);
        ll_in_sv = (LinearLayout)findViewById(R.id.linearLayout_in_scrollView);
        btnConfirm = (Button)findViewById(R.id.info_dialog_confirm);
        btnCancel = (Button)findViewById(R.id.info_dialog_cancel);
        btnNewList = (Button)buttonView.findViewById(R.id.info_dialog_new);
        today = Calendar.getInstance();

        // Get what user selected
        Intent intent = getIntent();
        taskItemSelected = intent.getIntExtra("taskItemSelected", -1);
        Log.i("taskItemSelected", String.valueOf(taskItemSelected));

        if(taskItemSelected == -1) {
            // do nothing
        } else {
            // User selected a task item to edit.
            // Backup goal for analyzing the differences of before and after
            // initialize two objects.
            old_mTask = new TaskItem();
            mTask = new TaskItem();
            // receive all object's data from fragment.
            old_mTask = (TaskItem) intent.getSerializableExtra("taskItem");
            // add old_mTask to mTask.
            mTask.setId(old_mTask.getId());
            mTask.setTitle(old_mTask.getTitle());
            mTask.setEarliestStartTime(old_mTask.getEarliestStartTime());
            mTask.setLatestStartTime(old_mTask.getLatestStartTime());
            mTask.setEarliestEndTime(old_mTask.getEarliestEndTime());
            mTask.setLatestEndTime(old_mTask.getLatestEndTime());
            mTask.setDeadline(old_mTask.getDeadline());
            mTask.setDuration(old_mTask.getDuration());
            mTask.setPreprocessList(old_mTask.getPreprocessList());
            mTask.setResourcesList(old_mTask.getResourcesList());
        }

        textTask.setText(mTask.getTitle());
        // make not editable.
        textTask.setKeyListener(null);
        // make strike through text.
        textTask.setPaintFlags(textTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        initializeListView();
        setActions();
    }

    private void initializeListView() {
        objectList = new ArrayList<HashMap>();
        int listViewId = 0;
        ll_in_sv.removeAllViews();

        //資料來源
        /**
         * This is the auto-generated list view, in TaskActivity it will auto show two
         * items, teach and collect result. As a guideline for user to know what kind of
         * tasks he/she can add.
         */
        for (int i = 0; i < 2; i++) {
            String title1 = "教";
            String title2 = "驗收成果";

            HashMap editMap = new HashMap();
            View view = LayoutInflater.from(TaskActivity.this).inflate(R.layout.task_object, null); //物件來源
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);

            // setting first item's title.
            editTask = (EditText) ll.findViewById(R.id.editTextTask);
            if (i == 0)
                editTask.setText(mTask.getTitle() + " - " + title1);
            else
                editTask.setText(mTask.getTitle() + " - " + title2);

            // setting first item's deadline.
            editDeadline = (EditText) ll.findViewById(R.id.editTextDeadline);
            long deadline = mTask.getDeadline();
            if (deadline != 0) {
                editDeadline.setText(millSecToDateConverter(deadline));
            } else {
                // If user didn't set any deadline  //目前不用理它?
            }

            // setting first item's duration
            editDuration = (EditText) ll.findViewById(R.id.editTextDuration);
            // fixme: prob don't need to set this, let the user decide.
            // But if we decide for the user might be better.
//        editDuration.setText(Long.toString(mTask.getTaskList().get(i).getDuration()));  // TODO: format

            editDeadline.setOnClickListener(deadlineClickHandler);
            editDeadline.setId(listViewId);
            editDuration.setOnClickListener(deadlineClickHandler);  //TODO: durationClickHandler
            editDuration.setId(listViewId);

            btnDelete = (Button) ll.findViewById(R.id.btn_del);
            btnDelete.setOnClickListener(deleteClickHandler);//設定監聽method
            btnDelete.setId(listViewId);//將按鈕帶入id 以供監聽時辨識使用
            listViewId = listViewId + 1;

            //將所有的元件放入map並存入list中
            editMap.put("TASK", editTask);
            editMap.put("DEADLINE", editDeadline);
            editMap.put("DURATION", editDuration);
            objectList.add(editMap);


            //將上面新建的例元件新增到主頁面的ll_in_sv中
            ll_in_sv.addView(view);
        }
        //最後一筆都放上新增按鈕
        ll_in_sv.addView(buttonView);
    }

    private void setActions() {

        // Confirm
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg", "Confirmed");
                // Check the goal text. It should not be empty.
                if(!"".equals(textTask.getText().toString().trim())) {
                    // Save data to buffer------------------------------------------------------------------------------------
                    saveDataInBuffer(); // save Task (in buffer)
                    mTask.setTitle(textTask.getText().toString());    // save Role (in buffer)

                    // Save buffer to SQLite------------------------------------------------------------------------------------
                    // Open db
                    dbHelper = new DBHelper(getApplicationContext());
                    db = dbHelper.getReadableDatabase();

                    // Did user add a goal or update a goal?
                    if(taskItemSelected == -1) {
                        // User selected to add a new role

//                        mTask.setId((int)rowId);    // We need to know the Role Id, and save it in Goal

                    } else {
                        // User selected to edit the role
                        saveTask();
                        deleteTask();
                        saveGoal();
                    }
                    // Return to update the date of RoleGoalFragment ----------------------------------------------------------------------
                    Intent intent = getIntent();    //取出上一個Activity傳過來的 Intent 物件。
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"尚未填寫「目標」",Toast.LENGTH_SHORT).show();   // TODO: English?
                }
            }
        });

        // Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg", "Backtrack");
                // TODO: AlertDialog
                finish();
            }
        });

        // New list
        btnNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg", "Add");
                // TODO: 數量限制?
                // We need to store data before adding, or data will be missing
                saveDataInBuffer();
//                mTask.addTaskItem(new TaskItem()); // fixme: dunno
                initializeListView(); //reload view
            }
        });
    }

    // Delete
    private View.OnClickListener deleteClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button delBtn =  (Button)v;
            int id = delBtn.getId();// Get the id which user click
            // We need to store data before delete, or data will be missing
            saveDataInBuffer();
//            mTask.getTaskList().remove(id); // fixme: dunno
            initializeListView(); //reload view
        }
    };

    // DeadlineEdit button click
    private View.OnClickListener deadlineClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get the deadline(Long)
            Long deadline = mTask.getDeadline();
            showDatePickerDialog(deadline, (EditText) v, v.getId());
        }
    };

    // Function for buffer---------------------------------------------------------------------------------------
    private void saveDataInBuffer() {
        int i = 0;
        for(HashMap editMap:objectList){
            String task = ((EditText)editMap.get("TASK")).getText().toString();
            String deadline = ((EditText)editMap.get("DEADLINE")).getText().toString();
            String duration = ((EditText)editMap.get("DURATION")).getText().toString();
            taskList.get(i).getId(); // It is important to identify each task    // If id is -1, it means the task is new
            taskList.get(i).setTitle(task);
            try {
                taskList.get(i).setDeadline(dateToMillSecConverter(deadline));
            } catch (ParseException e) {
                taskList.get(i).setDeadline(0);
                e.printStackTrace();
            }
            taskList.get(i).setDuration(Long.valueOf(duration)); // TODO: format
            i = i + 1;
        }
    }

    private void showDatePickerDialog(Long date, final EditText editText, final int id) {
        // If date is 0, it means user didn't set the deadline of goal (because the default of goal deadline is 0)
        if(date !=0) {
            mYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
            mMonth = Integer.parseInt(new SimpleDateFormat("MM").format(date)) - 1;
            mDay = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        }else {
            // show today
            mYear = today.get(Calendar.YEAR);
            mMonth = today.get(Calendar.MONTH);
            mDay = today.get(Calendar.DAY_OF_MONTH);
        }

        // DatePickerDialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Show the date user pick
                        SpannableString content = new SpannableString(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        Long deadline;
                        try {
                            deadline = dateToMillSecConverter(content.toString());
                        } catch (ParseException e) {
                            deadline = Long.valueOf(0);
                            e.printStackTrace();
                        }
                        // Why do we converted it twice(string > long > string)? Because we want the same format.
                        editText.setText(millSecToDateConverter(deadline));
                        // Store the date user picked into mTask
//                        mTask.getTaskList().get(id).setDeadline(deadline);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    //Function for Converter-------------------------------------------------------------------------------------
    private Long dateToMillSecConverter(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long m = sdf.parse(date).getTime();
        return m;
    }

    private String millSecToDateConverter(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date);
        return s;
    }

    // Function for DB-------------------------------------------------------------------------------------------
    private long saveGoal() {
        long rowId = mTask.getId();
        ContentValues cvr = new ContentValues();
        cvr.put(GOAL_TITLE, mTask.getTitle());
        cvr.put(GOAL_DEADLINE, mTask.getDeadline());
        cvr.put(GOAL_DURATION, mTask.getDuration());
        if(mTask.getId() == -1  && taskItemSelected == -1) {
            // Double check: User is adding a new role
//            rowId = db.insert(TABLE_NAME_ROLE_LIST, null, cvr);
        }else if(mTask.getId() != -1  && taskItemSelected != -1) {
            // User is modifying the role
            db.update(TABLE_NAME_GOAL_LIST, cvr,"_ID=" + String.valueOf(mTask.getId()), null);
        }else {
            // Something wrong?!
            Log.i("DB bug: ", " The role id may be modified? ");
        }
        return rowId;
    }

    private void deleteTask() {
        // Analyze the differences of before and after
        // If we don't find the old_goal after user editing, the old_goal should be deleted by user
        if(taskItemSelected != -1) {
            ArrayList<TaskItem> deleteList = new ArrayList<>();

//            for(int z = 0; z < old_mTask.getTaskList().size(); z++) {
//                deleteList.add(old_mTask.getTaskList().get(z));
//                for(int j = 0; j< mTask.getTaskList().size(); j++) {
//                    if(old_mTask.getTaskList().get(z).getId() == mTask.getTaskList().get(j).getId()) {
//                        // This goal was not be deleted by user
//                        deleteList.remove(old_mTask.getTaskList().get(z));
//                        break;
//                    }
//                }
//            }
            // Delete goal
            for(TaskItem taskItem: deleteList) {
                db.delete(TABLE_NAME_TASK_LIST,"_ID=" + String.valueOf(taskItem.getId()), null);
            }
        }
    }

    private void saveTask() {
        // Clean empty goals in buffer, get them, and insert or update to SQLite
        int index = 0;
        for(TaskItem taskItem: new ArrayList<TaskItem>()) {
//        for(TaskItem taskItem: mTask.getTaskList()) {
            String title = taskItem.getTitle();
            //Clean empty goal in buffer
            if(!"".equals(title.trim())) {
                // Title is not empty
                // Get data
                ContentValues cvg = new ContentValues();
                cvg.put(TASK_TITLE, taskItem.getTitle());
                cvg.put(TASK_DEADLINE, taskItem.getDeadline());
                cvg.put(TASK_DURATION, taskItem.getDuration());
                // insert or update
                if(taskItem.getId() == -1) {
                    // User is adding a new task
                    cvg.put(TASK_GOAL_ID, mTask.getId());   // Give goal the Goal Id
                    long rowTask =db.insert(TABLE_NAME_TASK_LIST, null, cvg);
//                    mTask.getTaskList().get(index).setId((int)rowTask);
                }else {
                    // User is modifying the task
                    db.update(TABLE_NAME_TASK_LIST, cvg,"_ID=" + String.valueOf(taskItem.getId()), null);
                }
            }
            index = index + 1;
        }
    }
}