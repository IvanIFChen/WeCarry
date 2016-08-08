package space.wecarry.wecarryapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.RoleItem;

public class RoleGoalActivity extends AppCompatActivity {
    EditText editName , editAddress , editPhone ,data_id;
    Button confirm, cancel,newList,btnDelect;
    LinearLayout ll_in_sv , objectLayout;
    ArrayList<HashMap> objectList;
    View buttonView;

    ArrayList<RoleItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_goal);
        buttonView = LayoutInflater.from(RoleGoalActivity.this).inflate(R.layout.role_goal_object_button, null);

        ll_in_sv = (LinearLayout)findViewById(R.id.linearLayout_in_scrollView);
        confirm = (Button)findViewById(R.id.info_dialog_confirm);
        cancel = (Button)findViewById(R.id.info_dialog_cancel);
        newList = (Button)buttonView.findViewById(R.id.info_dialog_new);

        getRoleGoal();

        addListView();
        setActions();
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

    private void addListView() {
        objectList = new ArrayList<HashMap>();
        int btnId = 0;
        ll_in_sv.removeAllViews();

        //資料來源
        for (int i = 0; i < mList.get(0).getGoalList().size(); i++) {

            HashMap<String,EditText> editMap = new HashMap();
            View view = LayoutInflater.from(RoleGoalActivity.this).inflate(R.layout.role_goal_object, null); //物件來源
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);

            editName = (EditText)ll.findViewById(R.id.editTextGoal);
            editName.setText(mList.get(0).getGoalList().get(i).getText());

            editPhone = (EditText)ll.findViewById(R.id.editTextDeadline);
            editPhone.setText(Long.toString(mList.get(0).getGoalList().get(i).getDeadline()));

            editAddress = (EditText)ll.findViewById(R.id.editTextImportance);
            editAddress.setText("IIIII:P");

            btnDelect = (Button)ll.findViewById(R.id.btn_del);
            btnDelect.setOnClickListener(clickHandler);//設定監聽method
            btnDelect.setId(btnId);//將按鈕帶入id 以供監聽時辨識使用
            btnId++;
            //將所有的元件放入map並存入list中
            editMap.put("NAME", editName);
            editMap.put("ADDRESS", editAddress);
            editMap.put("PHONE", editPhone);
            objectList.add(editMap);

            //將上面新建的例元件新增到主頁面的ll_in_sv中
            ll_in_sv.addView(view);
        }
        //最後一筆都放上新增按鈕
        ll_in_sv.addView(buttonView);
    }

    private void setActions() {
        //設定各元件的監聽
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("msg", "！！確定！！");
                //.....//儲存方式
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg", "！！返回！！");
                finish();
            }
        });

        newList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg", "！！新增！！");
                //在view中新增一筆新的list 你可以直接在 personal list 直接增加一筆然後再創立一次view
                mList.get(0).addGoalItem(new GoalItem());
                for(HashMap<String, EditText> editMap:objectList){
                    //我們Map中是存放EditText物件所以取出之後就像一般的物件使用喔
                    String name = editMap.get("NAME").getText().toString();
                }
                addListView();
            }
        });
    }

    //刪除
    private View.OnClickListener clickHandler= new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Button delBtn =  (Button)v; //在new 出所按下的按鈕
            int id = delBtn.getId();//獲取被點擊的按鈕的id
            objectList.get(id);//從 objectList得到此比資料
            //.....刪除list（略）
            Toast.makeText(getApplicationContext(),Integer.toString(id),Toast.LENGTH_SHORT).show();
            addListView(); //重新整理 view
        }
    };

}
