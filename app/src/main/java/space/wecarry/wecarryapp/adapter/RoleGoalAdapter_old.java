//package space.wecarry.wecarryapp.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//import space.wecarry.wecarryapp.R;
//import space.wecarry.wecarryapp.item.GoalItem;
//import space.wecarry.wecarryapp.item.RoleItem;
//
///**
// * Created by Blair on 2016/8/6.
// */
//public class RoleGoalAdapter_old extends BaseAdapter {
//
//    private ArrayList<RoleItem> roleList;
//    private LayoutInflater layoutInflater;
//    public Context context;
//
//    public RoleGoalAdapter_old(Context context, ArrayList roleList) {
//        this.context = context;
//        this.roleList = roleList;
//        this.layoutInflater = layoutInflater.from(context);
//    }
//
//
//    @Override
//    public int getCount() {
//        return roleList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return roleList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.listview_item_role_goal, null);
//
//            holder = new ViewHolder();
//            holder.textRole = (TextView) convertView.findViewById(R.id.textViewRole);
//            holder.textGoal = (TextView) convertView.findViewById(R.id.textViewGoal);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        //We set the tag to the TaskItem, so we can find it in callbacks
//        holder.textRole.setTag(roleList.get(position));
//        holder.textGoal.setTag(roleList.get(position));
//
//        //Set the text of the listview
//        holder.textRole.setText(roleList.get(position).getTitle());
//        //Get the all of goal
//        String temp = "";
//        int index = 1;
//        for(GoalItem gi : roleList.get(position).getGoalList()) {
//            temp += Integer.toString(index)+". " + gi.getTitle() + "\n";
//            index++;
//        }
//        holder.textGoal.setText(temp);
//
//        return convertView;
//    }
//
//    static class ViewHolder {
//        TextView textRole;
//        TextView textGoal;
//    }
//    //
//}
