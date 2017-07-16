package com.example.android.todolist;

/**
 * Created by Archita on 06-07-2017.
 */

public class Task {
    public boolean STATUS_COMPLETE;
    public String task;

    public Task() {
        STATUS_COMPLETE = false;
    }

    public Task(boolean STATUS_COMPLETE, String task) {
        super();
        this.STATUS_COMPLETE = STATUS_COMPLETE;
        this.task = task;
    }
}
