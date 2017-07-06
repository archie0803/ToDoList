package com.example.android.todolist;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import static android.R.attr.resource;

/**
 * Created by Archita on 06-07-2017.
 */

public class TaskListAdapter extends ArrayAdapter<String> {

    ArrayList<String> taskArrayList;
    Context context;

    public TaskListAdapter(@NonNull Context context, ArrayList<String> taskArrayList) {
        super(context, 0);
        this.taskArrayList = taskArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return taskArrayList.size();
    }
}
