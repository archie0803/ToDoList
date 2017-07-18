package com.example.android.todolist;

import static android.provider.Telephony.TextBasedSmsColumns.STATUS_COMPLETE;

/**
 * Created by Archita on 06-07-2017.
 */

public class Task {
    public int status;
    public String task;

    public Task() {
        status = 0;
    }

    public Task(int status, String task) {
        super();
        this.status = status;
        this.task = task;
    }
}
