package space.wecarry.wecarryapp.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import space.wecarry.wecarryapp.sqlite.DBHelper;

import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_DEADLINE;
import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_DURATION;
import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_IMPORTANCE;
import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_ROLE_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_TITLE;
import static space.wecarry.wecarryapp.sqlite.DBConstants.GOAL_URGENCY;
import static space.wecarry.wecarryapp.sqlite.DBConstants.ROLE_DEADLINE;
import static space.wecarry.wecarryapp.sqlite.DBConstants.ROLE_DURATION;
import static space.wecarry.wecarryapp.sqlite.DBConstants.ROLE_ID;
import static space.wecarry.wecarryapp.sqlite.DBConstants.ROLE_TITLE;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_GOAL_LIST;
import static space.wecarry.wecarryapp.sqlite.DBConstants.TABLE_NAME_ROLE_LIST;

public class RoleGoalActivity extends AppCompatActivity {
    private EditText editRole, editGoal, editDeadline;
    private Switch switchImportance, switchUrgency;
    private Button btnConfirm, btnCancel, btnNewList, btnDelete;
    private LinearLayout ll_in_sv;
    private ArrayList<HashMap> objectList;
    private View buttonView;
    private RoleItem mRole, old_mRole;
    private int mYear, mMonth, mDay;
    private Calendar today = null;
    private int roleUserSelected = -1; // -1 is new a role
    private DBHelper dbHelper = null;
    private SQLiteDatabase db = null;

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
            old_mRole = new RoleItem();
            mRole = new RoleItem();
            old_mRole = (RoleItem) intent.getSerializableExtra("roleItem");
            for(GoalItem goalItem: old_mRole.getGoalList()) {
                mRole.getGoalList().add(goalItem);
            }
            mRole.setTitle(old_mRole.getTitle());
            mRole.setDeadline(old_mRole.getDeadline());
            mRole.setDuration(old_mRole.getDuration());
            mRole.setId(old_mRole.getId());
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
            switchImportance.setChecked(mRole.getGoalList().get(i).isImportant());

            switchUrgency = (Switch)ll.findViewById(R.id.switchUrgency);
            switchUrgency.setChecked((mRole).getGoalList().get(i).isUrgent());

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
//                    // Tasting-------------------------------------------------------------------------------
//                    String goal ="";
//                    int i = 1;
//                    for(GoalItem goalItem:mRole.getGoalList()) {
//                        // Clean empty goal
//                        String g =goalItem.getTitle();
//                        if(!"".equals(g.trim())) {
//                            goal += Integer.toString(i) + ". " + g + "\n";
//                            i++;
//                        }
//                    }
//                    Toast.makeText(getApplicationContext(),"" +
//                            "角色: " +mRole.getTitle() +"\n"+
//                            "目標: " +"\n"+goal+
//                            "",Toast.LENGTH_SHORT).show();
////                    finish();
//                    // End tasting-----------------------------------------------------------------------------
                    // Save data in sqlite
                    // Open db
                    dbHelper = new DBHelper(getApplicationContext());
                    db = dbHelper.getReadableDatabase();

                    if(roleUserSelected == -1) {
                        // User selected to add a new role
                        long rowId = dbRole();
                        mRole.setId((int)rowId);    // We need to know the Role Id, and save it in Goal
                        cleanEmptyGoal();
                    } else {
                        // User selected to edit the role
                        cleanEmptyGoal();
//                        checkGoal();    // Check if user delete any goal
                        dbRole();
                    }
                    finish();
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
            int t =mRole.getGoalList().get(i).getId(); // It is important to identify each goal
            System.out.print(t);
            mRole.getGoalList().get(i).setTitle(goal);
            try {mRole.getGoalList().get(i).setDeadline(dateToMillsecConverter(deadline));
            } catch (ParseException e) {
                mRole.getGoalList().get(i).setDeadline(0);
                e.printStackTrace();
            }
            mRole.getGoalList().get(i).setImportant(importance);
            mRole.getGoalList().get(i).setUrgent(urgency);
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
                        // Store the date user picked into mRole
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

    // Function for DB-------------------------------------------------------------------------------------------
    private long dbRole() {
        // Role
        long rowId = -1;
        ContentValues cvr = new ContentValues(3);
        cvr.put(ROLE_TITLE, mRole.getTitle());
        cvr.put(ROLE_DEADLINE, mRole.getDeadline());
        cvr.put(ROLE_DURATION, mRole.getDuration());
        if(mRole.getId() == -1  && roleUserSelected == -1) {
            // Double check: User is adding a new role
            rowId = db.insert(TABLE_NAME_ROLE_LIST, null, cvr);
        }else if(mRole.getId() != -1  && roleUserSelected != -1) {
            // User is modifying the role
            db.update(TABLE_NAME_ROLE_LIST, cvr,"_ID=" + String.valueOf(mRole.getId()), null);
        }else {
            // Something wrong?!
            Log.i("DB bug: ", " The role id may be modified? ");
        }
        return rowId;
    }

    private void checkGoal() {
        // Check if user delete any goal
        if(roleUserSelected != -1) {
            // It means old_mRole isn't empty
            ArrayList<GoalItem> deleteList = old_mRole.getGoalList();
            for(int z=0; z < old_mRole.getGoalList().size(); z++) {
                for(int j=0; j<mRole.getGoalList().size(); j++) {
                    if(old_mRole.getGoalList().get(z).getId() == mRole.getGoalList().get(j).getId()) {
                        // This goal was not be deleted by user
                        deleteList.remove(old_mRole.getGoalList().get(z));
                        break;
                    }
                }
            }
            // Delete goal
            for(GoalItem goalItem: deleteList) {
                db.delete(TABLE_NAME_GOAL_LIST,"_ID=" + String.valueOf(goalItem.getId()), null);
            }
        }
    }

    private void cleanEmptyGoal() {
        // Goal
        // Clean empty goal ,and insert it or update it
        int index = 0;
        for(GoalItem goalItem:mRole.getGoalList()) {
            String title =goalItem.getTitle();
            if(!"".equals(title.trim())) {
//                goalItem = new GoalItem();      // It is important to default goalItem, or it can't insert in DB
//                mRole.addGoalItem(goalItem);    // It is for updating the data in GoalItem (for example, the duration need to be updated)
                ContentValues cvg = new ContentValues(6);
                cvg.put(GOAL_TITLE, goalItem.getTitle());
                cvg.put(GOAL_DEADLINE, goalItem.getDeadline());
//                cvg.put(GOAL_DURATION, goalItem.getDuration());  // I don't know why it has a bug????????????????????????????????????????????????
                cvg.put(GOAL_IMPORTANCE, String.valueOf(goalItem.isImportant()));
                cvg.put(GOAL_URGENCY, String.valueOf(goalItem.isUrgent()));

                // insert or update
                if(goalItem.getId() == -1) {
                    // Double check: User is adding a new goal
                    cvg.put(GOAL_ROLE_ID, mRole.getId());   // Give it Role Id
                    long rowGoal =db.insert(TABLE_NAME_GOAL_LIST, null, cvg);
                    mRole.getGoalList().get(index).setId((int)rowGoal);
                }else {
                    // User is modifying the goal
                    db.update(TABLE_NAME_GOAL_LIST, cvg,"_ID=" + String.valueOf(goalItem.getId()), null);
                }
            }else {
                // if the goal is empty
                mRole.getGoalList().remove(index);
            }
            index++;
        }
    }
}
