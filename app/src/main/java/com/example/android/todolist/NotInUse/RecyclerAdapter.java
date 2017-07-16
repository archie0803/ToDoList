package com.example.android.todolist.NotInUse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.todolist.R;

import java.util.ArrayList;

/**
 * Created by Archita on 12-07-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Note> mNotes;
    private NotesClickListener mListener;

    public interface NotesClickListener {
        void onItemClick(View view, int position);

        void onRemoveClicked(int position);
    }


    public RecyclerAdapter(Context context, ArrayList<Note> notes, NotesClickListener listener) {
        mContext = context;
        mNotes = notes;
        mListener = listener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_note, parent, false);
        return new NoteViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder1((NoteViewHolder) holder, position);
    }

    public void onBindViewHolder1(NoteViewHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.descTextView.setText(note.getDescription());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        TextView descTextView;
        Button removeButton;
        NotesClickListener mNotesClickListener;

        public NoteViewHolder(View itemView, NotesClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNotesClickListener = listener;
            titleTextView = itemView.findViewById(R.id.title_text);
            descTextView = itemView.findViewById(R.id.desc_text);
            removeButton = itemView.findViewById(R.id.remove_btn);
            removeButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (id == R.id.note_layout) {
                    mNotesClickListener.onItemClick(view, position);
                } else if (id == R.id.remove_btn) {
                    mNotesClickListener.onRemoveClicked(position);
                }
            }

        }
    }

}
