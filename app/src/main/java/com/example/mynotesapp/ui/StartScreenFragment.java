package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

import java.util.Objects;

public class StartScreenFragment extends Fragment implements View.OnClickListener {

    private boolean isLand;

    private Fragment notesFr;

    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button showNotesList = view.findViewById(R.id.view_list_notes);
        Button newNote = view.findViewById(R.id.add_note);
        Button settings = view.findViewById(R.id.settings);
        Button aboutApp = view.findViewById(R.id.about_app);

        showNotesList.setOnClickListener(this);
        newNote.setOnClickListener(this);
        settings.setOnClickListener(this);
        aboutApp.setOnClickListener(this);

        notesFr = new NotesFragment();

        fragmentManager = getParentFragmentManager();

        isLand = getResources().getBoolean(R.bool.is_landscape);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_list_notes) {
            if (isLand) {
                removeInPrimContIfNotEmpty();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container_left, notesFr)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, notesFr)
                        .commit();
            }

        } else if (v.getId() == R.id.add_note) {
            Toast.makeText(getActivity(), "new note button is pressed!", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.settings) {
            Toast.makeText(getActivity(), "settings button is pressed!", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.about_app) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, new AboutFragment())
                    .commit();
        }
    }

    private void removeInPrimContIfNotEmpty() {
        if (fragmentManager.findFragmentById(R.id.fragment_container) != null) {
            fragmentManager.beginTransaction()
                    .remove(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragment_container)))
                    .commit();
        }
    }
}