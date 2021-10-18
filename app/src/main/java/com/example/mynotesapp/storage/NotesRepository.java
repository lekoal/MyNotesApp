package com.example.mynotesapp.storage;

import com.example.mynotesapp.domain.Note;

import java.util.List;

public interface NotesRepository {

    List<Note> getNotes();
}
