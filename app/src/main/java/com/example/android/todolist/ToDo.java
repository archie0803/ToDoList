package com.example.android.todolist;

import java.util.ArrayList;

/**
 * Created by Archita on 22-06-2017.
 */

public class ToDo {

    int id;
    String title;
    ArrayList<String> tasks;

    public ToDo(int id, String title, ArrayList<String> tasks) {
        this.id = id;
        this.title = title;
        this.tasks = tasks;
    }
}
