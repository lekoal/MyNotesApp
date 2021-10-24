package com.example.mynotesapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

import java.util.Objects;

public class StartScreenFragment extends Fragment {

    NotesFragment listNotesFragment;
    FragmentTransaction fTr;

    private static final String ARG_NOTE = "ARG_NOTE";

    private Note selectedNote;

    private View startButtonContainer;

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
        Button addNote = view.findViewById(R.id.add_note);
        Button showMenu = view.findViewById(R.id.show_menu);
        Button aboutApp = view.findViewById(R.id.about_app);

        onClList(listNotes);
        onClList(addNote);
        onClList(showMenu);
        onClList(aboutApp);


        startButtonContainer = view.findViewById(R.id.start_screen_button_container);

        FragmentManager fragmentManager = getChildFragmentManager();

        if (!(fragmentManager.findFragmentById(R.id.child_container) instanceof NotesFragment)) {
            fragmentManager.popBackStack();
        }

        boolean isLandscape = getResources().getBoolean(R.bool.is_landscape);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_NOTE)) {
            selectedNote = savedInstanceState.getParcelable(ARG_NOTE);

            if (isLandscape) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ARG_NOTE, selectedNote);

            } else {
                NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(selectedNote);

                fragmentManager.beginTransaction()
                        .replace(R.id.child_container, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        getChildFragmentManager().setFragmentResultListener(NotesFragment.KEY_NOTES_LIST_ACTIVITY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                selectedNote = result.getParcelable(NotesFragment.ARG_NOTE);

                if (isLandscape) {

                    fragmentManager.setFragmentResult(NoteDetailsFragment.KEY_NOTES_LIST_DETAILS, result);

                    NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(selectedNote);

                    fragmentManager.beginTransaction()
                            .replace(R.id.child_container2, detailsFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(selectedNote);

                    fragmentManager.beginTransaction()
                            .replace(R.id.child_container, detailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    public void onClList(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.view_list_notes:
                        startButtonContainer.setVisibility(View.INVISIBLE);
                        listNotesFragment = new NotesFragment();
                        fTr = getChildFragmentManager().beginTransaction();
                        fTr.replace(R.id.child_container, listNotesFragment);
                        fTr.addToBackStack(null);
                        fTr.commit();
                        break;

                    case R.id.add_note:
                        Toast.makeText(getActivity(), "new note button is pressed!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.show_menu:
                        Toast.makeText(getActivity(), "show menu button is pressed!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.about_app:
                        Toast.makeText(getActivity(), "about application button is pressed!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (selectedNote != null) {
            outState.putParcelable(ARG_NOTE, selectedNote);
        }
        super.onSaveInstanceState(outState);
    }
}