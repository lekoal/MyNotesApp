package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;
import com.example.mynotesapp.storage.CreatedNotesRepository;

import java.util.List;
import java.util.Objects;

public class NotesFragment extends Fragment implements NotesListView {

    public static final String KEY_NOTES_LIST_ACTIVITY = "KEY_NOTES_LIST_ACTIVITY";
    public static final String ARG_NOTE = "ARG_NOTE";

    private NotesListPresenter presenter;

    private boolean isLand;

    private FragmentManager fragmentManager;

    private Note selectedNote;

    private NotesAdapter adapter;

    private ProgressBar progressBar;

    private Button tryAgainButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new NotesListPresenter(this, new CreatedNotesRepository());

        adapter = new NotesAdapter();
        adapter.setNoteClicked(new NotesAdapter.OnNoteClicked() {
            @Override
            public void onNoteClicked(Note note) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ARG_NOTE, note);

                getParentFragmentManager().setFragmentResult(KEY_NOTES_LIST_ACTIVITY, bundle);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView notesList = view.findViewById(R.id.notes_root);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        notesList.setLayoutManager(layoutManager);

        notesList.setAdapter(adapter);

        fragmentManager = getParentFragmentManager();

        isLand = getResources().getBoolean(R.bool.is_landscape);

        progressBar = view.findViewById(R.id.progress_bar);

        tryAgainButton = view.findViewById(R.id.try_again_button);

        presenter.requestNotes();

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.requestNotes();
            }
        });

        if (isLand) {
            if (!(fragmentManager.findFragmentById(R.id.fragment_container_left) instanceof NotesFragment)) {
                fragmentManager.popBackStack();
            }
        } else {
            if (!(fragmentManager.findFragmentById(R.id.fragment_container) instanceof NotesFragment)) {
                fragmentManager.popBackStack();
            }
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_NOTE)) {
            selectedNote = savedInstanceState.getParcelable(ARG_NOTE);

            NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(selectedNote);
            if (isLand) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ARG_NOTE, selectedNote);
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        fragmentManager.setFragmentResultListener(NotesFragment.KEY_NOTES_LIST_ACTIVITY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                selectedNote = result.getParcelable(NotesFragment.ARG_NOTE);

                NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(selectedNote);
                if (isLand) {
                    removeInPrimContIfNotEmpty();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.fragment_container_right, detailsFragment)
                            .commit();
                } else {
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.fragment_container, detailsFragment)
                            .commit();
                }
            }
        });
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideTryAgainButton() {
        tryAgainButton.setVisibility(View.GONE);
    }

    @Override
    public void showTryAgainButton() {
        tryAgainButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNotes(List<Note> notes) {
        adapter.setNotes(notes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (selectedNote != null) {
            outState.putParcelable(ARG_NOTE, selectedNote);
        }
        super.onSaveInstanceState(outState);
    }

    private void removeInPrimContIfNotEmpty() {
        if (fragmentManager.findFragmentById(R.id.fragment_container) != null && isLand) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .remove(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragment_container)))
                    .commit();
        }
    }
}