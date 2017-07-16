package com.example.android.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Archita on 28-06-2017.
 */

public class ToDoRecyclerAdapter extends RecyclerView.Adapter<ToDoRecyclerAdapter.ToDoViewHolder> {

    ArrayList<ToDo> toDoArrayList;
    Context context;
    private ToDoClickListener mListener;

    @Override
    public ToDoRecyclerAdapter.ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ToDoViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ToDoRecyclerAdapter.ToDoViewHolder holder, int position) {
        ToDo toDo = toDoArrayList.get(position);
        holder.titleTextView.setText(toDo.getTitle());
        holder.descTextView.setText(toDo.getTasks());
    }

    @Override
    public int getItemCount() {
        return toDoArrayList.size();
    }

    public interface ToDoClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public static class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView titleTextView;
        TextView descTextView;
        ToDoClickListener mToDoClickListener;

        public ToDoViewHolder(View itemView, ToDoClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mToDoClickListener = listener;
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (id == R.id.todo_layout) {
                    mToDoClickListener.onItemClick(view, position);
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (id == R.id.todo_layout) {
                    Log.d("ID: ", position + " RA");
                    mToDoClickListener.onItemLongClick(view, position);
                }
            }
            return true;
        }
    }


    public ToDoRecyclerAdapter(Context context, ArrayList<ToDo> toDoArrayList, ToDoClickListener listener) {
        this.toDoArrayList = toDoArrayList;
        this.context = context;
        this.mListener = listener;
    }


}
