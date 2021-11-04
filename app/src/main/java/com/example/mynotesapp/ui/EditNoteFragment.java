package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

import java.util.Objects;

public class EditNoteFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";
    public static final String KEY_RESULT = "KEY_RESULT";
    public static final String ARG_TITLE = "ARG_TITLE";
    public static final String ARG_CONTENT = "ARG_CONTENT";
    public static final String ARG_ID = "ARG_ID";

    private String title;
    private String id;
    private String date;
    private String time;
    private String content;

    private EditText editTitle;
    private TextView noteTime;
    private TextView noteDate;
    private EditText editContent;

    private Button cancel;
    private Button save;

    private boolean isLand;

    private FragmentManager fragmentManager;

    public static EditNoteFragment newInstance(Note note) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_NOTE, note);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Note note = requireArguments().getParcelable(ARG_NOTE);

        id = note.getId();
        date = note.getDate();
        time = note.getTime();
        title = note.getTitle();
        content = note.getContent();

        editTitle = view.findViewById(R.id.edit_title_text);

        noteDate = view.findViewById(R.id.note_date);
        noteTime = view.findViewById(R.id.note_time);

        editContent = view.findViewById(R.id.edit_content_text);

        editContent.setMovementMethod(new ScrollingMovementMethod());

        fragmentManager = getParentFragmentManager();

        cancel = view.findViewById(R.id.edit_button_cancel);
        save = view.findViewById(R.id.edit_button_save);

        isLand = getResources().getBoolean(R.bool.is_landscape);

        editTitle.setText(title);
        noteTime.setText(time);
        noteDate.setText(date);
        editContent.setText(content);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelled();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });
    }

    private void onCancelled() {
        if (isLand) {
            if (fragmentManager.findFragmentById(R.id.fragment_container_right) != null) {
                fragmentManager.beginTransaction()
                        .remove(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragment_container_right)))
                        .commit();
            }
        }
        fragmentManager.popBackStack();
    }

    private void onSave() {
        String titleChangedText = editTitle.getText().toString();
        String contentChangedText = editContent.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_ID, id);
        bundle.putString(ARG_TITLE, titleChangedText);
        bundle.putString(ARG_CONTENT, contentChangedText);

        getParentFragmentManager().setFragmentResult(KEY_RESULT, bundle);


        NotesFragment notesFragment = new NotesFragment();
        if (isLand) {
            removeInPrimContIfNotEmpty();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_right, notesFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, notesFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void removeInPrimContIfNotEmpty() {
        if (fragmentManager.findFragmentById(R.id.fragment_container_right) != null) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .remove(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragment_container_right)))
                    .commit();
        }
    }
}