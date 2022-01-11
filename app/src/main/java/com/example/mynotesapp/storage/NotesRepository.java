package com.example.mynotesapp.storage;

import com.example.mynotesapp.domain.Note;

import java.util.List;

public interface NotesRepository {

    void getNotes(Callback<List<Note>> callback);
    void clear(Callback<Void> callback);
    void add(String title, String content, Callback<Note> callback);
    void update(String id, String title, String content, Callback<Note> callback);
    void delete(Note note, Callback<Void> callback);
}
