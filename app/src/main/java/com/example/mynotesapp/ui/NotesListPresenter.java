package com.example.mynotesapp.ui;

import com.example.mynotesapp.domain.Note;
import com.example.mynotesapp.storage.Callback;
import com.example.mynotesapp.storage.NotesRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NotesListPresenter {

    private final NotesListView view;

    private final NotesRepository repository;

    public NotesListPresenter(NotesListView view, NotesRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void requestNotes() {

        view.showProgress();

        repository.getNotes(new Callback<List<Note>>() {
            @Override
            public void onSuccess(List<Note> result) {
                view.showNotes(result);
                view.hideProgress();
                view.hideTryAgainButton();
            }

            @Override
            public void onError(Throwable error) {
                view.hideProgress();
                view.showTryAgainButton();
            }
        });
    }

    public void removeAll() {
        view.showProgress();

        repository.clear(new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.clearNotes();
                view.hideProgress();
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    public void add(String noteTitle, String noteContent) {
        view.showProgress();
        repository.add(noteTitle, noteContent, new Callback<Note>() {
            @Override
            public void onSuccess(Note result) {
                view.addNote(result);
                view.hideProgress();
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    public void delete(Note selectedNoteContext) {
        view.showProgress();
        repository.delete(selectedNoteContext, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.deleteNote(selectedNoteContext);
                view.hideProgress();
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    public void update(String id, String title, String content, Note selectedNoteContext) {
        view.showProgress();

        repository.update(id, title, content, new Callback<Note>() {
            @Override
            public void onSuccess(Note result) {
                view.updateNote(result);
                view.hideProgress();
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }
}
