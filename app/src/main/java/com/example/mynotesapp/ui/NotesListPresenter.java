package com.example.mynotesapp.ui;

import com.example.mynotesapp.domain.Note;
import com.example.mynotesapp.storage.Callback;
import com.example.mynotesapp.storage.NotesRepository;

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
            }

            @Override
            public void onError(Throwable error) {
                view.hideProgress();
            }
        });
    }
}
