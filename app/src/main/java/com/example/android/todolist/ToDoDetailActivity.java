package com.example.android.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.todolist.ToDoOpenHelper.TASK_ID;
import static com.example.android.todolist.ToDoOpenHelper.TASK_TABLE_NAME;
import static com.example.android.todolist.ToDoOpenHelper.TASK_TITLE;
import static com.example.android.todolist.ToDoOpenHelper.TODO_TABLE_NAME;
import static com.example.android.todolist.ToDoOpenHelper.todoOpenHelper;

public class ToDoDetailActivity extends AppCompatActivity {

    public static int id = -1;
    EditText titleTextView;
    ArrayList<String> taskList;
    ListView taskListView;
    Button Submit;
    SQLiteDatabase database;
    TaskListAdapter taskAdapt;
    TextView addTasks;
    static EditText taskText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_detail);

        todoOpenHelper = ToDoOpenHelper.getOpenHelperInstance(ToDoDetailActivity.this);


        titleTextView = (EditText) findViewById(R.id.title);
        taskList = new ArrayList<>();
        taskListView = (ListView) findViewById(R.id.taskListView);

        taskAdapt = new TaskListAdapter(this, taskList);
        taskListView.setAdapter(taskAdapt);

        Intent i = getIntent();
        id = i.getIntExtra(IntentConstants.TODO_ID, -1);
        if (id != -1) {
            String prevTitle = i.getStringExtra(IntentConstants.TODO_TITLE);
            titleTextView.setText(prevTitle);

            //String prevDescription = i.getStringExtra(IntentConstants.TASK_TITLE);
            //descriptionTextView.setText(prevDescription);
            database = todoOpenHelper.getReadableDatabase();
            String col[] = {TASK_ID, TASK_TITLE};
            Cursor cursor = database.query(TASK_TABLE_NAME, col, ToDoOpenHelper.TODO_ID + " = " + id, null, null, null, null);
            while (cursor.moveToNext()) {
                //Display the arrayList content
                String task = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TASK_TITLE));
                taskList.add(task);
                taskAdapt.notifyDataSetChanged();
            }
        }
        Log.d("VALUE", id + "");

        taskText = (EditText) findViewById(R.id.taskText);
        addTasks = (TextView) findViewById(R.id.addTasks) ;

        addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add a layout, then edit its editText and then save/update in database after click
                // of another Add More or Submit button and notify database change
                String newTask = "";
                taskList.add(newTask);
                taskAdapt.notifyDataSetChanged();
                taskText.setText(newTask);
//
//                ContentValues cv = new ContentValues();
//                cv.put(ToDoOpenHelper.TODO_TITLE, newTask);
//
//                database.update(TASK_TABLE_NAME, cv, ToDoOpenHelper.TODO_ID + " = " + id, null);
//                SQLiteDatabase database = todoOpenHelper.getWritableDatabase();
//                database.insert(TASK_TABLE_NAME, null, null);
            }
        });

        Submit = (Button) findViewById(R.id.submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = titleTextView.getText().toString();
                //String newCategory = categoryTextView.getText().toString();

                if (newTitle.trim().isEmpty()) {
                    titleTextView.setError("Can't be empty");
                }
                /*if (newTasks.trim().isEmpty()) {
                    titleTextView.setError("Can't be empty");
                }*/


                SQLiteDatabase database = todoOpenHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(ToDoOpenHelper.TODO_TITLE, newTitle);
                //cv.put(ToDoOpenHelper.EXPENSE_CATEGORY, newCategory);
                //cv.put(ToDoOpenHelper.EXPENSE_PRICE, newPrice);
                if (id == -1) {
                    database.insert(TODO_TABLE_NAME, null, cv);
                } else if (id > 0) {
                    database.update(TODO_TABLE_NAME, cv, ToDoOpenHelper.TODO_ID + " = " + id, null);
                }


                setResult(RESULT_OK);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
