package space.wecarry.wecarryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.item.GoalItem;
import space.wecarry.wecarryapp.item.TaskItem;

/**
 * Created by Blair on 2016/8/13.
 */
public class PlanAdapter extends BaseAdapter {

    private ArrayList<GoalItem> goalList;
    private LayoutInflater layoutInflater;
    public Context context;

    public PlanAdapter(Context context, ArrayList goalList) {
        this.context = context;
        this.goalList = goalList;
        this.layoutInflater = layoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return goalList.size();
    }

    @Override
    public Object getItem(int position) {
        return goalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_item_plan, null);

            holder = new ViewHolder();
            holder.textGoal = (TextView) convertView.findViewById(R.id.textViewGoal);
            holder.textTask = (TextView) convertView.findViewById(R.id.textViewTask);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //We set the tag to the TaskItem, so we can find it in callbacks
        holder.textGoal.setTag(goalList.get(position));
        holder.textTask.setTag(goalList.get(position));

        //Set the text of the listview
        holder.textGoal.setText(goalList.get(position).getTitle());
        //Get the all of task
        String temp = "";
        int index = 1;
        for(TaskItem taskItem : goalList.get(position).getTaskList()) {
            temp += Integer.toString(index)+". " + taskItem.getTitle() + "\n";
            index++;
        }
        holder.textTask.setText(temp);

        return convertView;
    }

    static class ViewHolder{
        TextView textGoal;
        TextView textTask;
    }
}
