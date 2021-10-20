package com.example.mynotesapp.ui;

import com.example.mynotesapp.domain.Note;

import java.util.List;

public interface NotesListView {

    void showNotes(List<Note> notes);
}
