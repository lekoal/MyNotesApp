package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mynotesapp.R;

public class StartScreenFragment extends Fragment {

    NotesFragment listNotesFragment;
    FragmentTransaction fTr;

    public StartScreenFragment() {
        super(R.layout.fragment_start_screen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button listNotes = view.findViewById(R.id.view_list_notes);
        listNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listNotesFragment = new NotesFragment();
                fTr = getChildFragmentManager().beginTransaction();
                fTr.replace(R.id.child_container, listNotesFragment, "listOfNotes");
                fTr.addToBackStack("listOfNotes");
                fTr.commit();
            }
        });
    }
}