package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.View;
import android.widget.TextView;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

public class NoteDetailsFragment extends Fragment {

    public static final String ARG_NOTE = "ARG_NOTE";

    public static final String KEY_NOTES_LIST_DETAILS = "KEY_NOTES_LIST_DETAILS";

    public NoteDetailsFragment() {
        super(R.layout.fragment_note_details);
    }

    public static NoteDetailsFragment newInstance(Note note) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);

        NoteDetailsFragment fragment = new NoteDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView noteTitle = view.findViewById(R.id.note_title);
        TextView noteDate = view.findViewById(R.id.note_date);
        TextView noteTime = view.findViewById(R.id.note_time);
        TextView noteContent = view.findViewById(R.id.note_content);

        if (getArguments() != null && getArguments().containsKey(ARG_NOTE)) {
            Note note = getArguments().getParcelable(ARG_NOTE);

            noteTitle.setText(note.getTitle());
            noteDate.setText(note.getDate());
            noteTime.setText(note.getTime());
            noteContent.setText(note.getContent());
        }

        getParentFragmentManager().setFragmentResultListener(NoteDetailsFragment.KEY_NOTES_LIST_DETAILS, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Note note1 = result.getParcelable(ARG_NOTE);

                noteTitle.setText(note1.getTitle());
                noteDate.setText(note1.getDate());
                noteTime.setText(note1.getTime());
                noteContent.setText(note1.getContent());
            }
        });

    }
}