package com.example.android.todolist;

import java.util.ArrayList;

/**
 * Created by Archita on 22-06-2017.
 */

public class ToDo {

    int id;
    private String title;
    private String tasks;
    private long alarmTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public ToDo(int id, String title, String tasks, long alarmTime) {
        this.id = id;
        this.title = title;
        this.tasks = tasks;
        this.alarmTime = alarmTime;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }
}
