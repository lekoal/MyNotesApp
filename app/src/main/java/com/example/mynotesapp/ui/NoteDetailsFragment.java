package com.example.mynotesapp.ui;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

import java.util.Calendar;
import java.util.Objects;

public class NoteDetailsFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";

    public static final String ARG_NOTE_FOR_DELETE = "ARG_NOTE_FOR_DELETE";
    public static final String DELETE_RESULT = "DELETE_RESULT";

    private String newDate;
    private int selectedYear = 2021;
    private int selectedMonth = 10;
    private int selectedDayOfMonth = 18;

    private FragmentManager fragmentManager;

    private EditNoteFragment editNoteFragment;

    private Button editContent;

    private boolean isLand;

    private Note selectedNote;

    public static NoteDetailsFragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        NoteDetailsFragment fragment = new NoteDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView noteTitle = view.findViewById(R.id.note_title);
        TextView noteDate = view.findViewById(R.id.note_date);
        TextView noteTime = view.findViewById(R.id.note_time);
        TextView noteContent = view.findViewById(R.id.note_content);
        noteContent.setMovementMethod(new ScrollingMovementMethod());

        editContent = view.findViewById(R.id.edit_content);

        isLand = getResources().getBoolean(R.bool.is_landscape);

        fragmentManager = getParentFragmentManager();

        if (getArguments() != null && getArguments().containsKey(ARG_NOTE)) {
            selectedNote = getArguments().getParcelable(ARG_NOTE);
            noteTitle.setText(selectedNote.getTitle());
            noteDate.setText(selectedNote.getDate());
            noteTime.setText(selectedNote.getTime());
            noteContent.setText(selectedNote.getContent());

            editNoteFragment = EditNoteFragment.newInstance(selectedNote);
        }

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                newDate = dayOfMonth + "." + monthOfYear + "." + year;
                if (getArguments() != null && getArguments().containsKey(ARG_NOTE)) {
                    Note note = getArguments().getParcelable(ARG_NOTE);
                    note.setDate(newDate);
                    noteDate.setText(newDate);
                }
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

        Button editDate = view.findViewById(R.id.edit_date);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();

            }
        });

        editContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isLand) {
                    removeInPrimContIfNotEmpty();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_right, editNoteFragment)
                            .commit();
                } else {
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.fragment_container, editNoteFragment)
                            .commit();
                }
            }
        });

        Button deleteNote = view.findViewById(R.id.delete_note);

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Delete key is  pressed", Toast.LENGTH_SHORT).show();
            }
        });
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