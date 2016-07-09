package com.traviswkim.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by traviswkim on 7/8/16.
 */
public class TodosAdapter extends ArrayAdapter<Todo>{
    private static class ViewHolder {
        TextView TaskName;
        TextView DueDate;
        TextView Priority;
    }
    public TodosAdapter(Context context, ArrayList<Todo> todos) {
        super(context, R.layout.item_todo, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Todo aTodo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_todo, parent, false);
            viewHolder.TaskName = (TextView) convertView.findViewById(R.id.tvTaskName);
            viewHolder.DueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
            viewHolder.Priority = (TextView) convertView.findViewById(R.id.tvPriority);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.TaskName.setText(aTodo.taskName);
        viewHolder.DueDate.setText(aTodo.dueDate);
        viewHolder.Priority.setText(aTodo.priority);
        // Return the completed view to render on screen
        return convertView;
    }


}
