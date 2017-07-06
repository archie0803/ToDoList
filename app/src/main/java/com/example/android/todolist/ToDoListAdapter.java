package com.example.android.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Archita on 28-06-2017.
 */

public class ToDoListAdapter extends ArrayAdapter<ToDo> {

    ArrayList<ToDo> toDoArrayList;
    Context context;

    public ToDoListAdapter(@NonNull Context context, ArrayList<ToDo> toDoArrayList) {
        super(context, 0);
        this.toDoArrayList = toDoArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return toDoArrayList.size();
        //return super.getCount();
    }

    static class ToDoViewHolder {
        TextView titleTV;
        TextView descTV;


        ToDoViewHolder(TextView titleTV, TextView descTV) {
            this.titleTV = titleTV;
            this.descTV = descTV;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            TextView titleTV = (TextView) convertView.findViewById(R.id.titleTextView);
            TextView descTV = (TextView) convertView.findViewById(R.id.descTextView);
            ToDoViewHolder toDoViewHolder = new ToDoViewHolder(titleTV, descTV);
            convertView.setTag(toDoViewHolder);
        }
        ToDoViewHolder toDoViewHolder = (ToDoViewHolder) convertView.getTag();
        ToDo e = toDoArrayList.get(position);
        toDoViewHolder.titleTV.setText(e.title);
        //toDoViewHolder.descTV.setText(e.tasks);
        return convertView;
    }
}
