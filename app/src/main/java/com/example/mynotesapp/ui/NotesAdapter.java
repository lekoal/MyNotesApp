package com.example.mynotesapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    public int deleteNote(Note selectedNoteContext) {
        int index = notes.indexOf(selectedNoteContext);
        notes.remove(index);
        return index;
    }

    public int updateNote(Note result) {
        int index = -1;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(result.getId())) {
                index = i;
                break;
            }
        }
        notes.set(index, result);
        return index;
    }

    interface OnNoteClicked {
        void onNoteClicked(Note note);

        void onNoteLongClicked(View itemView, Note note);
    }

    private final List<Note> notes = new ArrayList<>();

    private OnNoteClicked noteClicked;

    private Fragment fragment;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setNotes(Collection<Note> toSet) {
        notes.clear();
        notes.addAll(toSet);
    }

    public void addNote(Note result) {
        notes.add(result);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note, viewGroup, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position) {
        Note note = notes.get(position);

        noteViewHolder.getNoteTitle().setText(note.getTitle());
        noteViewHolder.getNoteDate().setText(note.getDate());
        noteViewHolder.getNoteTime().setText(note.getTime());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public OnNoteClicked getNoteClicked() {
        return noteClicked;
    }

    public void setNoteClicked(OnNoteClicked noteClicked) {
        this.noteClicked = noteClicked;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView noteTitle;
        private TextView noteDate;
        private TextView noteTime;

        public TextView getNoteTitle() {
            return noteTitle;
        }

        public TextView getNoteDate() {
            return noteDate;
        }

        public TextView getNoteTime() {
            return noteTime;
        }

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            fragment.registerForContextMenu(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getNoteClicked() != null) {
                        getNoteClicked().onNoteClicked(notes.get(getAdapterPosition()));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (getNoteClicked() != null) {
                        getNoteClicked().onNoteLongClicked(view, notes.get(getAdapterPosition()));
                    }
                    return true;
                }
            });

            noteTitle = itemView.findViewById(R.id.note_title);
            noteDate = itemView.findViewById(R.id.note_date);
            noteTime = itemView.findViewById(R.id.note_time);
        }
    }
}
