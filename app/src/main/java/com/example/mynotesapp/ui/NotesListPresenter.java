package com.example.mynotesapp.ui;

import com.example.mynotesapp.storage.NotesRepository;

public class NotesListPresenter {

    private final NotesListView view;

    private final NotesRepository repository;

    public NotesListPresenter(NotesListView view, NotesRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void requestNotes() {
        view.showNotes(repository.getNotes());
    }
}
