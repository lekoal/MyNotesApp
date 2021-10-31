package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

public class EditNoteFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";

    private final String title;
    private final String date;
    private final String time;
    private final String content;

    private EditText editTitle;
    private TextView noteTime;
    private TextView noteDate;
    private EditText editContent;

    private Button cancel;
    private Button save;

    private FragmentManager fragmentManager;

    private Note selectedNote;

    public EditNoteFragment(String title, String date, String time, String content) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.content = content;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTitle = view.findViewById(R.id.edit_title_text);
        noteDate = view.findViewById(R.id.note_date);
        noteTime = view.findViewById(R.id.note_time);
        editContent = view.findViewById(R.id.edit_content_text);
        editContent.setMovementMethod(new ScrollingMovementMethod());

        fragmentManager = getParentFragmentManager();

        cancel = view.findViewById(R.id.edit_button_cancel);
        save = view.findViewById(R.id.edit_button_save);

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
        fragmentManager.popBackStack();
    }

    private void onSave() {
        String titleChangedText = editTitle.getText().toString();
        String contentChangedText = editContent.getText().toString();

//            selectedNote.setTitle(titleChangedText);
//            selectedNote.setContent(contentChangedText);
//
//            NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(selectedNote);
//
//            fragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, detailsFragment)
//                    .addToBackStack(null)
//                    .commit();
    }
}