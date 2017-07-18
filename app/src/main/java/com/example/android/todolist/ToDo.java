package com.example.android.todolist;

import java.util.ArrayList;

/**
 * Created by Archita on 22-06-2017.
 */

public class ToDo {

    int id;
    private String title;
    private String desc;
    private long alarmTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTasks() {
        return desc;
    }

    public void setTasks(String tasks) {
        this.desc = tasks;
    }

    public ToDo(int id, String title, String tasks, long alarmTime) {
        this.id = id;
        this.title = title;
        this.desc = tasks;
        this.alarmTime = alarmTime;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }
}
