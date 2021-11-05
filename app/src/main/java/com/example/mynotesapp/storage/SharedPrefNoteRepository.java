package com.example.mynotesapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mynotesapp.domain.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SharedPrefNoteRepository implements NotesRepository {

    private final SharedPreferences sharedPreferences;

    private final Gson gson = new Gson();

    private static final String ARG_NOTES_LIST = "ARG_NOTES_LIST";

    private final Type type = new TypeToken<ArrayList<Note>>() {}.getType();

    public SharedPrefNoteRepository(Context context) {

        sharedPreferences = context.getSharedPreferences("SharedPrefNoteRepository", Context.MODE_PRIVATE);

    }

    @Override
    public void getNotes(Callback<List<Note>> callback) {
        String storedValues = sharedPreferences.getString(ARG_NOTES_LIST, "[]");
        ArrayList<Note> notes = gson.fromJson(storedValues, type);
        callback.onSuccess(notes);
    }

    @Override
    public void clear(Callback<Void> callback) {
        String storedValues = sharedPreferences.getString(ARG_NOTES_LIST, "[]");
        ArrayList<Note> notes = gson.fromJson(storedValues, type);
        notes.clear();
        sharedPreferences.edit().clear().apply();
        callback.onSuccess(null);
    }

    @Override
    public void add(String title, String content, Callback<Note> callback) {
        Note note = new Note(UUID.randomUUID().toString(), title, content);
        String storedValues = sharedPreferences.getString(ARG_NOTES_LIST, "[]");
        ArrayList<Note> notes = gson.fromJson(storedValues, type);
        notes.add(note);
        sharedPreferences.edit().putString(ARG_NOTES_LIST, gson.toJson(notes, type)).apply();
        callback.onSuccess(note);
    }

    @Override
    public void update(String id, String title, String content, Callback<Note> callback) {
        Note note = new Note(id, title, content);
        String storedValues = sharedPreferences.getString(ARG_NOTES_LIST, "[]");
        ArrayList<Note> notes = gson.fromJson(storedValues, type);
        notes.add(note);
        int index = -1;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            notes.remove(index);
            notes.set(index, note);
        }

        sharedPreferences.edit().putString(ARG_NOTES_LIST, gson.toJson(notes, type)).apply();
        callback.onSuccess(note);
    }

    @Override
    public void delete(Note note, Callback<Void> callback) {
        String storedValues = sharedPreferences.getString(ARG_NOTES_LIST, "[]");
        ArrayList<Note> notes = gson.fromJson(storedValues, type);
        notes.remove(note);
        sharedPreferences.edit().putString(ARG_NOTES_LIST, gson.toJson(notes, type)).apply();
        callback.onSuccess(null);
    }
}
