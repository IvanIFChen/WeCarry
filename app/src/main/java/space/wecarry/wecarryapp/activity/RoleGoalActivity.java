package space.wecarry.wecarryapp.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
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
//    private ArrayList<RoleItem> mList;
    private RoleItem mRole;
    private int mYear, mMonth, mDay;
    private Calendar today = null;
    private int roleUserSelected = -1; // -1 is new a role

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
        today = Calendar.getInstance();

        // Get what user selected
        Intent intent = getIntent();
        roleUserSelected = intent.getIntExtra("roleUserSelected", -1);
        Log.i("roleUserSelected", String.valueOf(roleUserSelected));

        if(roleUserSelected == -1) {
            // User selected to add a new role
            mRole = new RoleItem();
        } else {
            // User selected to edit the role
            mRole = (RoleItem) intent.getSerializableExtra("roleItem");
        }

        editRole.setText(mRole.getTitle());
        addListView();
        setActions();
    }

    private void addListView() {
        objectList = new ArrayList<HashMap>();
        int listViewId = 0;
        ll_in_sv.removeAllViews();

        //資料來源
        for (int i = 0; i < mRole.getGoalList().size(); i++) {

            HashMap editMap = new HashMap();
            View view = LayoutInflater.from(RoleGoalActivity.this).inflate(R.layout.role_goal_object, null); //物件來源
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);

            editGoal = (EditText)ll.findViewById(R.id.editTextGoal);
            editGoal.setText(mRole.getGoalList().get(i).getTitle());

            editDeadline = (EditText)ll.findViewById(R.id.editTextDeadline);
            long deadline = mRole.getGoalList().get(i).getDeadline();
            if(deadline !=0) {
                editDeadline.setText(millsecToDateConverter(deadline));
            }else {
                // If user didn't set any deadline  //目前不用理它?
            }
            editDeadline.setOnClickListener(deadlineClickHandler);
            editDeadline.setId(listViewId);

            switchImportance = (Switch)ll.findViewById(R.id.switchImportance);
            switchImportance.setChecked(mRole.getGoalList().get(i).isImportance());

            switchUrgency = (Switch)ll.findViewById(R.id.switchUrgency);
            switchUrgency.setChecked((mRole).getGoalList().get(i).isUrgency());

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
                // Check it
                if(!"".equals(editRole.getText().toString().trim())) {
                    // Pass (User has input role)
                    saveDataInBuffer();
                    mRole.setTitle(editRole.getText().toString());    // save Role (in buffer)

                    // Tasting-------------------------------------------------------------------------------
                    String goal ="";
                    int i = 1;
                    for(GoalItem goalItem:mRole.getGoalList()) {
                        // Clean empty goal
                        String g =goalItem.getTitle();
                        if(!"".equals(g.trim())) {
                            goal += Integer.toString(i) + ". " + g + "\n";
                            i++;
                        }
                    }
                    Toast.makeText(getApplicationContext(),"" +
                            "角色: " +mRole.getTitle() +"\n"+
                            "目標: " +"\n"+goal+
                            "",Toast.LENGTH_SHORT).show();
//                    finish();
                    // End tasting-----------------------------------------------------------------------------
                    // TODO: Clean empty goal
                    // TODO: Save data in sqlite
                }else {
                    Toast.makeText(getApplicationContext(),"尚未填寫「角色」",Toast.LENGTH_SHORT).show();   // TODO: English?
                }
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
                // TODO: 數量限制?
                // We need to store data before adding, or data will be missing
                saveDataInBuffer();
                mRole.addGoalItem(new GoalItem());
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
            mRole.getGoalList().remove(id);
            addListView(); //reload view
        }
    };

    //DeadlineClick
    private View.OnClickListener deadlineClickHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // Get the deadline(Long)
            Long deadline = mRole.getGoalList().get(v.getId()).getDeadline();
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
            mRole.getGoalList().get(i).setTitle(goal);
            try {mRole.getGoalList().get(i).setDeadline(dateToMillsecConverter(deadline));
            } catch (ParseException e) {
                mRole.getGoalList().get(i).setDeadline(0);
                e.printStackTrace();
            }
            mRole.getGoalList().get(i).setImportance(importance);
            mRole.getGoalList().get(i).setUrgency(urgency);
            i++;
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
                            deadline = dateToMillsecConverter(content.toString());
                        } catch (ParseException e) {
                            deadline = Long.valueOf(0);
                            e.printStackTrace();
                        }
                        // Why do we converted it twice(string > long > string)? Because we want the same format.
                        editText.setText(millsecToDateConverter(deadline));
                        // Store the date user picked into mList
                        mRole.getGoalList().get(id).setDeadline(deadline);

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
