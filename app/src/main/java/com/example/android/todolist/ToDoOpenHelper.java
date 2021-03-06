package com.example.android.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Archita on 02-07-2017.
 */

public class ToDoOpenHelper extends SQLiteOpenHelper {
    public static final String TODO_TABLE_NAME = "Todo";
    public static final String TODO_ID = "todo_id";
    public static final String TODO_TITLE = "todo_title";
    public static final String TASK_TABLE_NAME = "Tasks";
    public static final String TASK_TITLE = "task_title";
    public static final String TASK_ID = "task_id";
    public static final String TASK_STATUS = "task_status";
    public static final String TODO_DESCRIPTION = "todo_desc";
    public static final String TODO_ALARM_TIME = "alarm_time";

    public static ToDoOpenHelper todoOpenHelper;

    public static ToDoOpenHelper getOpenHelperInstance(Context context) {
        if (todoOpenHelper == null) {
            todoOpenHelper = new ToDoOpenHelper(context);
        }
        return todoOpenHelper;
    }

    private ToDoOpenHelper(Context context) {
        super(context, "Todo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "Create table " + TODO_TABLE_NAME + " (" +
                TODO_ID + " integer primary key autoincrement, " +
                TODO_TITLE + " text, " + TODO_ALARM_TIME + " long);";

        db.execSQL(query1);


        String query2 = "Create table " + TASK_TABLE_NAME + " ( " +
                TASK_ID + " integer primary key autoincrement, " +
                TODO_ID + " integer, " +
                TASK_TITLE + " text, " +
                TASK_STATUS + " integer default 0);";

        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
