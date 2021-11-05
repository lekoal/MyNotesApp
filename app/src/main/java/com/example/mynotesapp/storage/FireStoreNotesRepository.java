package com.example.mynotesapp.storage;

import androidx.annotation.NonNull;

import com.example.mynotesapp.domain.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreNotesRepository implements NotesRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String NOTES = "notes";

    private static final String TITLE = "title";

    private static final String CONTENT = "content";

    @Override
    public void getNotes(Callback<List<Note>> callback) {
        db.collection(NOTES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            ArrayList<Note> result = new ArrayList<>();

                            for (DocumentSnapshot document : documents) {

                                String noteId = document.getId();
                                String noteTitle = document.getString(TITLE);
                                String noteContent = document.getString(CONTENT);

                                Note note = new Note(noteId, noteTitle, noteContent);

                                result.add(note);
                            }

                            callback.onSuccess(result);
                        } else {
                            callback.onError(task.getException());
                        }
                    }
                });
    }

    @Override
    public void clear(Callback<Void> callback) {

        db.collection(NOTES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            ArrayList<Note> result = new ArrayList<>();

                            for (DocumentSnapshot document : documents) {

                                String noteId = document.getId();
                                String noteTitle = document.getString(TITLE);
                                String noteContent = document.getString(CONTENT);

                                Note note = new Note(noteId, noteTitle, noteContent);

                                result.add(note);
                            }

                            for (Note note : result) {
                                db.collection(NOTES)
                                        .document(note.getId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    callback.onSuccess(null);
                                                } else {
                                                    callback.onError(task.getException());
                                                }
                                            }
                                        });
                            }

                        }
                    }
                });
    }

    @Override
    public void add(String title, String content, Callback<Note> callback) {

        Map<String, Object> data = new HashMap<>();

        data.put(TITLE, title);
        data.put(CONTENT, content);

        db.collection(NOTES)
                .add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Note note = new Note(task.getResult().getId(), title, content);
                            callback.onSuccess(note);
                        } else {
                            callback.onError(task.getException());
                        }
                    }
                });

    }

    @Override
    public void update(String id, String title, String
            content, Callback<Note> callback) {

        Map<String, Object> data = new HashMap<>();

        data.put(TITLE, title);
        data.put(CONTENT, content);

        db.collection(NOTES)
                .document(id)
                .update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(new Note(id, title, content));
                        } else {
                            callback.onError(task.getException());
                        }
                    }
                });

    }

    @Override
    public void delete(Note note, Callback<Void> callback) {

        db.collection(NOTES)
                .document(note.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(null);
                        } else {
                            callback.onError(task.getException());
                        }
                    }
                });

    }
}
