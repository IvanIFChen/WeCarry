package space.wecarry.wecarryapp.activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;

public class RoleGoalActivity extends AppCompatActivity {
    private EditText editRole, editGoal, editDeadline;
    private Switch switchImportance, switchUrgency;
    private Button btnConfirm, btnCancel, btnNewList, btnDelete;
    private LinearLayout ll_in_sv;
    private ArrayList<HashMap> objectList;
    private View buttonView;
    private ArrayList<RoleItem> mList;
    private int mYear, mMonth, mDay;
    private int roleIndex = 0; // User select in RoleGoalFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_goal);
        buttonView = LayoutInflater.from(RoleGoalActivity.this).inflate(R.layout.role_goal_object_button, null);
        editRole = (EditText)findViewById(R.id.editTextRole);
        ll_in_sv = (LinearLayout)findViewById(R.id.linearLayout_in_scrollView);
        btnConfirm = (Button)findViewById(R.id.info_dialog_confirm);
        btnCancel = (Button)findViewById(R.id.info_dialog_cancel);
        btnNewList = (Button)buttonView.findViewById(R.id.info_dialog_new);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        roleIndex = 0; // User select in RoleGoalFragment   //Testing   //TODO
        getRoleGoal();  // Get data from DB, and save in mList
        editRole.setText(mList.get(0).getText());
        addListView();
        setActions();
    }

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
    }

    private void addListView() {
        objectList = new ArrayList<HashMap>();
        int listViewId = 0;
        ll_in_sv.removeAllViews();

        //資料來源
        for (int i = 0; i < mList.get(0).getGoalList().size(); i++) {

            HashMap editMap = new HashMap();
            View view = LayoutInflater.from(RoleGoalActivity.this).inflate(R.layout.role_goal_object, null); //物件來源
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);

            editGoal = (EditText)ll.findViewById(R.id.editTextGoal);
            editGoal.setText(mList.get(roleIndex).getGoalList().get(i).getText());

            // TODO: If user didn't set any deadline?
            editDeadline = (EditText)ll.findViewById(R.id.editTextDeadline);
            long deadline = mList.get(roleIndex).getGoalList().get(i).getDeadline();
            if(deadline !=0) {
                editDeadline.setText(millsecToDateConverter(deadline));
            }else {
                editDeadline.setText("未設定");
            }
            editDeadline.setOnClickListener(deadlineClickHandler);
            editDeadline.setId(listViewId);

            switchImportance = (Switch)ll.findViewById(R.id.switchImportance);
            switchImportance.setChecked(mList.get(roleIndex).getGoalList().get(i).isImportance());

            switchUrgency = (Switch)ll.findViewById(R.id.switchUrgency);
            switchUrgency.setChecked((mList.get(roleIndex)).getGoalList().get(i).isUrgency());

            btnDelete = (Button)ll.findViewById(R.id.btn_del);
            btnDelete.setOnClickListener(deleteClickHandler);//設定監聽method
            btnDelete.setId(listViewId);//將按鈕帶入id 以供監聽時辨識使用
            listViewId++;
            //將所有的元件放入map並存入list中
            editMap.put("GOAL", editGoal);
            editMap.put("DEADLINE", editDeadline);
            editMap.put("IMPORTANCE", switchImportance);
            editMap.put("URGENCY", switchUrgency);
            objectList.add(editMap);

            //將上面新建的例元件新增到主頁面的ll_in_sv中
            ll_in_sv.addView(view);
        }
        //最後一筆都放上新增按鈕
        ll_in_sv.addView(buttonView);
    }

    private void setActions() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("msg", "Confirmed");
                // TODO: Save data in sqlite
                saveDataInBuffer();
                mList.get(roleIndex).setText(editRole.getText().toString());    // save Role
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg", "Backtrack");
                // TODO: AlertDialog
                finish();
            }
        });

        btnNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg", "Add");
                // We need to store data before adding, or data will be missing
                saveDataInBuffer();
                mList.get(roleIndex).addGoalItem(new GoalItem());
                addListView(); //reload view
            }
        });
    }

    //Delete
    private View.OnClickListener deleteClickHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Button delBtn =  (Button)v;
            int id = delBtn.getId();// Get the id which user click
//            objectList.get(id);//從 objectList得到此筆資料
            // We need to store data before delete, or data will be missing
            int i = 0;
            saveDataInBuffer();
            mList.get(roleIndex).getGoalList().remove(id);
            addListView(); //reload view
        }
    };

    //DeadlineClick
    private View.OnClickListener deadlineClickHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // Get the deadline(Long)
            Long deadline = mList.get(roleIndex).getGoalList().get(v.getId()).getDeadline();
            showDatePickerDialog(deadline, (EditText) v, v.getId());
        }
    };

    private void saveDataInBuffer() {
        int i = 0;
        for(HashMap editMap:objectList){
            String goal = ((EditText)editMap.get("GOAL")).getText().toString();
            String deadline = ((EditText)editMap.get("DEADLINE")).getText().toString();
            boolean importance = ((Switch)editMap.get("IMPORTANCE")).isChecked();
            boolean urgency = ((Switch)editMap.get("URGENCY")).isChecked();
            mList.get(roleIndex).getGoalList().get(i).setText(goal);
            try {mList.get(roleIndex).getGoalList().get(i).setDeadline(dateToMillsecConverter(deadline));
            } catch (ParseException e) {
                mList.get(roleIndex).getGoalList().get(i).setDeadline(0);
                e.printStackTrace();
            }
            mList.get(roleIndex).getGoalList().get(i).setImportance(importance);
            mList.get(roleIndex).getGoalList().get(i).setUrgency(urgency);
            i++;
        }
    }

    private void showDatePickerDialog(Long date, final EditText editText, final int id) {
        //TODO: If the date is 0 ?
        // If date is 0, it means user didn't set the deadline of goal
        if(date !=0) {
            mYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
            mMonth = Integer.parseInt(new SimpleDateFormat("MM").format(date)) - 1;
            mDay = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        }
        Log.i("year",Integer.toString(mYear));
        Log.i("month",Integer.toString(mMonth));
        Log.i("day",Integer.toString(mDay));
        // DatePickerDialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Show the date user pick
                        SpannableString content = new SpannableString(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//                        Log.i("year",Integer.toString(year));
//                        Log.i("month",Integer.toString(monthOfYear + 1));
//                        Log.i("day",Integer.toString(dayOfMonth));
                        editText.setText(content);
                        // Store the date user picked into mList
                        try {
                            mList.get(roleIndex).getGoalList().get(id).setDeadline(
                                    dateToMillsecConverter(content.toString()));
                        } catch (ParseException e) {
                            mList.get(roleIndex).getGoalList().get(id).setDeadline(0);
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    private Long dateToMillsecConverter(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long m = sdf.parse(date).getTime();
        return m;
    }

    private String millsecToDateConverter(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date);
        return s;
    }
}
