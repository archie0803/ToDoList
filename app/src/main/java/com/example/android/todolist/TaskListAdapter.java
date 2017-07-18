package com.example.android.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;


/**
 * Created by Archita on 06-07-2017.
 */

public class TaskListAdapter extends ArrayAdapter<String> {

    ArrayList<Task> taskArrayList;
    Context context;

    public TaskListAdapter(@NonNull Context context, ArrayList<Task> taskArrayList) {
        super(context, 0);
        this.taskArrayList = taskArrayList;
        this.context = context;
    }

    static class TaskViewHolder {
        CheckBox status;
        EditText taskDetail;


        TaskViewHolder(CheckBox status, EditText taskDetail) {
            this.status = status;

            this.taskDetail = taskDetail;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_list_view, null);
            CheckBox status = (CheckBox) convertView.findViewById(R.id.taskCheck);
            EditText taskDetail = (EditText) convertView.findViewById(R.id.taskText);
            taskDetail.setText("");
            TaskViewHolder taskViewHolder = new TaskViewHolder(status, taskDetail);
            convertView.setTag(taskViewHolder);
        }
        final TaskViewHolder taskViewHolder = (TaskViewHolder) convertView.getTag();
        final Task t = taskArrayList.get(position);
        taskViewHolder.status.setChecked(t.STATUS_COMPLETE);
        taskViewHolder.taskDetail.setText(t.task);
        taskViewHolder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskViewHolder.status.setChecked(true);
                t.STATUS_COMPLETE = true;
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return taskArrayList.size();
    }
}
