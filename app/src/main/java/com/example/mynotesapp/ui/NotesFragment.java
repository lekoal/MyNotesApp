package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;
import com.example.mynotesapp.storage.NoteStorage;
import com.example.mynotesapp.storage.NotesRepository;

import java.util.List;

public class NotesFragment extends Fragment implements NotesListView {

    public static final String KEY_NOTES_LIST_ACTIVITY = "KEY_NOTES_LIST_ACTIVITY";
    public static final String ARG_NOTE = "ARG_NOTE";

    private LinearLayout notesListRoot;

    private NotesListPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new NotesListPresenter(this, new NoteStorage());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notesListRoot = view.findViewById(R.id.notes_root);

        presenter.requestNotes();
    }

    @Override
    public void showNotes(List<Note> notes) {

        for (Note note : notes) {
            View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_note, notesListRoot, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ARG_NOTE, note);

                    getParentFragmentManager().setFragmentResult(KEY_NOTES_LIST_ACTIVITY, bundle);
                }
            });

            TextView noteTitle = itemView.findViewById(R.id.note_title);
            noteTitle.setText(note.getTitle());

            TextView noteDate = itemView.findViewById(R.id.note_date);
            noteDate.setText(note.getDate());

            TextView noteTime = itemView.findViewById(R.id.note_time);
            noteTime.setText(note.getTime());

            notesListRoot.addView(itemView);
        }
    }
}