package com.example.android.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.android.todolist.R.id.taskText;
import static com.example.android.todolist.ToDoOpenHelper.TASK_ID;
import static com.example.android.todolist.ToDoOpenHelper.TASK_STATUS;
import static com.example.android.todolist.ToDoOpenHelper.TASK_TABLE_NAME;
import static com.example.android.todolist.ToDoOpenHelper.TASK_TITLE;
import static com.example.android.todolist.ToDoOpenHelper.TODO_TABLE_NAME;
import static com.example.android.todolist.ToDoOpenHelper.todoOpenHelper;

public class ToDoDetailActivity extends AppCompatActivity {

    int id = -1;
    EditText titleTextView;
    ArrayList<Task> taskList;
    ListView taskListView;
    Button Submit;
    SQLiteDatabase database;
    TaskListAdapter taskAdapt;
    TextView addTasks;

    TextView alarmDateTime;
    Button setAlarm;
    Calendar calendar;
    long epoch;
    String newDate;
    String newTime;
    String finalAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_detail);

        calendar = Calendar.getInstance();

        todoOpenHelper = ToDoOpenHelper.getOpenHelperInstance(ToDoDetailActivity.this);


        titleTextView = (EditText) findViewById(R.id.title);
        taskList = new ArrayList<>();
        taskListView = (ListView) findViewById(R.id.taskListView);

        taskAdapt = new TaskListAdapter(this, taskList);
        taskListView.setAdapter(taskAdapt);

        addTasks = (TextView) findViewById(R.id.addTasks);
        setAlarm = (Button) findViewById(R.id.alarm_btn);
        alarmDateTime = (TextView) findViewById(R.id.alarm_date_time);


        // EDIT OR NEW
        Intent i = getIntent();
        id = i.getIntExtra(IntentConstants.TODO_ID, -1);
        Log.d("ID: ", id + " DA");
        if (id != -1) {
            //TITLE
            String prevTitle = i.getStringExtra(IntentConstants.TODO_TITLE);
            titleTextView.setText(prevTitle);

            //DATE AND TIME
            epoch = i.getLongExtra(IntentConstants.TODO_ALARM_TIME, 0);
            Date date = new Date();
            date.setTime(epoch);
            calendar.setTime(date);
            calendar.setTimeInMillis(epoch);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            day = day - 1;
            String newAlarmTime = "" + hour + ":" + minute;
            String newAlarmDate = day + "/" + (month + 1) + "/" + year;
            alarmDateTime.setText("Alarm Date: " + newAlarmDate + "\nAlarm Time: " + newAlarmTime);

            //ADD TASKS
            loadTasks();
        }
        Log.d("VALUE", id + "");

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                alarmDateTime.setText("Alarm Date: " + newDate + "\nAlarm Time: " + newTime);
                showDatePicker(ToDoDetailActivity.this, year, month, day);
                //String finalTime = showTimePicker(ToDoDetailActivity.this, hour, minute);


            }
        });


        addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Add More clicked - added to ArrayList and display.
                //Submit clicked - added to database


                String addedTask;
                //String desc = "";
                if (taskList.size() >= 1) {
                    int lastPosition = taskListView.getAdapter().getCount() - 1;
                    View v = taskListView.getChildAt(lastPosition);
                    EditText taskText = v.findViewById(R.id.taskText);
                    addedTask = taskText.getText().toString();
                    Task t = taskList.get(lastPosition);
                    t.task = addedTask;
                    t.status = 0;
                    Task t1 = new Task();
                    taskList.add(t1);
                    taskListView.setSelection(taskList.size() - 1);
                    //desc += addedTask + " ,";
                } else {
                    Task t = new Task();
                    taskList.add(t);
                }
                taskAdapt.notifyDataSetChanged();

            }
        });

        //DONE EXCEPT THE TASKS PORTION.
        Submit = (Button) findViewById(R.id.submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = titleTextView.getText().toString();

                if (newTitle.trim().isEmpty()) {
                    titleTextView.setError("Can't be empty");
                    titleTextView.requestFocus();
                    return;
                }

                SQLiteDatabase database = todoOpenHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(ToDoOpenHelper.TODO_TITLE, newTitle);
                cv.put(ToDoOpenHelper.TODO_ALARM_TIME, epoch);

                if (id == -1) {
                    database.insert(TODO_TABLE_NAME, null, cv);
                    for (int i = 0; i < taskList.size(); i++) {
                        ContentValues cv2 = new ContentValues();
                        cv2.put(todoOpenHelper.TASK_TITLE, taskList.get(i).task);
                        cv2.put(ToDoOpenHelper.TASK_STATUS, taskList.get(i).status);
                        database.insert(TASK_TABLE_NAME, null, cv2);
                    }
                } else if (id > 0) {
                    database.update(TODO_TABLE_NAME, cv, ToDoOpenHelper.TODO_ID + " = " + id, null);
                    for (int i = 0; i < taskList.size(); i++) {
                        ContentValues cv2 = new ContentValues();
                        cv2.put(todoOpenHelper.TASK_TITLE, taskList.get(i).task);
                        cv2.put(ToDoOpenHelper.TASK_STATUS, taskList.get(i).status);
                        database.replace(TASK_TABLE_NAME, null, cv2);
                    }

                }

                AlarmManager am = (AlarmManager) ToDoDetailActivity.this.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(ToDoDetailActivity.this, AlarmReceiver.class);
                intent.putExtra(IntentConstants.TODO_TITLE, newTitle);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ToDoDetailActivity.this, 1, intent, 0);
                am.set(AlarmManager.RTC, epoch, pendingIntent);


                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void loadTasks() {
        todoOpenHelper = ToDoOpenHelper.getOpenHelperInstance(this);
        taskList.clear();
        database = todoOpenHelper.getReadableDatabase();
        String col[] = {TASK_ID, TASK_TITLE, TASK_STATUS};
        Cursor cursor = database.query(TASK_TABLE_NAME, col, ToDoOpenHelper.TODO_ID + " = " + id, null, null, null, null);
        //Cursor cursor = database.query(TASK_TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TASK_TITLE));
            int status = cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.TASK_STATUS));
            Task t = new Task(status, title);
            Log.d("TASK ", "STATUS: " + status);
            Log.d("TASK ", "TITLE: " + title);
            taskList.add(t);
        }
        taskAdapt.notifyDataSetChanged();
    }


    private void showTimePicker(Context context, int hour, int minute) {
        TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                newTime = "" + selectedHour + ":" + selectedMinute;
                alarmDateTime.setText("Alarm Date: " + newDate + "\nAlarm Time: " + newTime);
                calendar.set(Calendar.HOUR, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                calendar.set(Calendar.SECOND, 0);
                epoch = calendar.getTimeInMillis();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        alarmDateTime.setVisibility(View.VISIBLE);
        mTimePicker.show();

    }

    private void showDatePicker(Context context, int initialYear, int initialMonth, int initialDay) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int initialYear, int initialMonth, int initialDay) {
                        calendar.set(initialYear, initialMonth, initialDay);
                        newDate = initialDay + "/" + (initialMonth + 1) + "/" + initialYear;
                        Log.d("date in textView", newDate);
                        calendar.get(Calendar.HOUR_OF_DAY);
                    }

                }, initialYear, initialMonth, initialDay);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        showTimePicker(ToDoDetailActivity.this, hour, minute);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();

        //String selectedTime = showTimePicker(ToDoDetailActivity.this, hour, minute);
        finalAlarm = "Alarm Date: " + newDate + "\nAlarm Time: " + newTime;
        alarmDateTime.setText("Alarm Date: " + newDate + "\nAlarm Time: " + newTime);
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
