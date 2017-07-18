package com.example.android.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.attr.category;
import static android.R.attr.id;
import static com.example.android.todolist.ToDoOpenHelper.TASK_TABLE_NAME;
import static com.example.android.todolist.ToDoOpenHelper.TODO_ID;
import static com.example.android.todolist.ToDoOpenHelper.TODO_TABLE_NAME;
import static com.example.android.todolist.ToDoOpenHelper.todoOpenHelper;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_TODO = 1;
    //ListView listView;
    ArrayList<ToDo> toDoList;
    ToDoRecyclerAdapter toDoRecyclerAdapter;
    SQLiteDatabase database;
    public static int pos;
    FloatingActionButton fab;

    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoOpenHelper = ToDoOpenHelper.getOpenHelperInstance(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        toDoList = new ArrayList<>();

        fab = (FloatingActionButton) findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ToDoDetailActivity.class);
                i.putExtra(IntentConstants.TODO_ID, -1);
                startActivityForResult(i, NEW_TODO);
            }
        });

        toDoRecyclerAdapter = new ToDoRecyclerAdapter(this, toDoList, new ToDoRecyclerAdapter.ToDoClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                pos = position;
                Intent i = new Intent(MainActivity.this, ToDoDetailActivity.class);
                i.putExtra(IntentConstants.TODO_ID, toDoList.get(position).id);
                Log.d("ID: ", toDoList.get(position).id + " MA");
                i.putExtra(IntentConstants.TODO_TITLE, toDoList.get(position).getTitle());
                if (toDoList.get(position).getAlarmTime() != 0) {
                    Log.d("ID: ", toDoList.get(position).getAlarmTime() + " AT");
                    i.putExtra(IntentConstants.TODO_ALARM_TIME, toDoList.get(position).getAlarmTime());
                }
                startActivityForResult(i, 1);
                toDoRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                int i_d = toDoList.get(position).id;
                remove(position, i_d);
            }
        });
        mRecyclerView.setAdapter(toDoRecyclerAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        /*
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(toDoList,from,to);
                toDoRecyclerAdapter.notifyItemMoved(from,to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //int position = viewHolder.getAdapterPosition();
                //toDoList.remove(position);
                //mAdapter.notifyItemRemoved(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        */

        updateTasks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                updateTasks();
            } else if (resultCode == RESULT_CANCELED) {
                //
            }
        }

    }

    public void remove(int pos, int i_d) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete?");
        builder.setCancelable(true);
        final int position = pos;
        final int id = i_d;
        //builder.setMessage("Are you sure you wanna delete?");
        View v = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final TextView tv = v.findViewById(R.id.conText);
        tv.setText("Are you sure you want to delete?");
        builder.setView(v);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toDoList.remove(position);
                toDoRecyclerAdapter.notifyDataSetChanged();
                database = todoOpenHelper.getWritableDatabase();
                database.delete(TODO_TABLE_NAME, ToDoOpenHelper.TODO_ID + " = " + id, null);
                //Cursor c = database.query()
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateTasks() {
        todoOpenHelper = ToDoOpenHelper.getOpenHelperInstance(this);
        toDoList.clear();
        ArrayList<String> tasks = new ArrayList<>();
        String desc = "";
        database = todoOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(TODO_TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TODO_TITLE));
            int id = cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.TODO_ID));
            long alarmTime = cursor.getLong(cursor.getColumnIndex(ToDoOpenHelper.TODO_ALARM_TIME));
            Cursor cursor2 = database.query(TASK_TABLE_NAME, null, ToDoOpenHelper.TODO_ID + " = " + id, null, null, null, null);
            while (cursor2.moveToNext()) {
                String task = cursor2.getString(cursor2.getColumnIndex(ToDoOpenHelper.TASK_TITLE));
                tasks.add(task);
                desc += task + ", ";
            }
            ToDo t = new ToDo(id, title, desc, alarmTime);
            toDoList.add(t);
            cursor2.close();
        }
        toDoRecyclerAdapter.notifyDataSetChanged();
        int size = toDoList.size();
        mRecyclerView.smoothScrollToPosition(size);
        cursor.close();
    }

}

/*
ADD MENU -
AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add?");
            builder.setCancelable(false);
            View v = getLayoutInflater().inflate(R.layout.custom_add, null);
            final TextView tv = (TextView) v.findViewById(R.id.tv);
            tv.setText("What should the title, category and price be?");
            builder.setView(v);
            final EditText name = (EditText) v.findViewById(R.id.name);
            final EditText cat = (EditText) v.findViewById(R.id.cat);
            final EditText price = (EditText) v.findViewById(R.id.price);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ToDo e1 = new ToDo();
                    e1.title = name.getText().toString();
                    e1.category = cat.getText().toString();
                    e1.price = Integer.parseInt(price.getText().toString());
                    toDoList.add(e1);
                    toDoListAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
 */

/*

    FOR LOOP TO INSERT PROPERTIES THROUGH OBJECT

    for (int i = 0; i < 20; i++) {
            ToDo e = new ToDo();
            //e.title = "Expense " + (i + 1);
            //e.price = 100 + i * 10;
            //e.category = "Food";
            toDoList.add(e);
        }

 */


/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                Intent i = new Intent(MainActivity.this, ToDoDetailActivity.class);
                i.putExtra(IntentConstants.TODO_ID, toDoList.get(position).id);
                i.putExtra(IntentConstants.TODO_TITLE, toDoList.get(position).title);
                startActivityForResult(i, 1);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                database = todoOpenHelper.getWritableDatabase();
                id = toDoList.get(position).id;
                remove(position);
                database.delete(TODO_TABLE_NAME, ToDoOpenHelper.TODO_ID + " = " + id, null);
                return true;
            }
        });
        */

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.remove == id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete?");
            builder.setCancelable(false);

            //builder.setMessage("Are you sure you wanna delete?");
            View v = getLayoutInflater().inflate(R.layout.dialog_view, null);
            final TextView tv = (TextView) v.findViewById(R.id.conText);
            tv.setText("Are you sure you want to delete?");
            builder.setView(v);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    toDoList.remove(toDoList.size() - 1);
                    toDoRecyclerAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return true;
    }
    */
