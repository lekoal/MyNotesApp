package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;
import com.example.mynotesapp.storage.CreatedNotesRepository;
import com.example.mynotesapp.storage.FireStoreNotesRepository;
import com.example.mynotesapp.storage.SharedPrefNoteRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NotesFragment extends Fragment implements NotesListView {

    public static final String KEY_NOTES_LIST_ACTIVITY = "KEY_NOTES_LIST_ACTIVITY";
    public static final String ARG_NOTE = "ARG_NOTE";

    private NotesListPresenter presenter;

    private boolean isLand;

    private FragmentManager fragmentManager;

    private Note selectedNote;

    private Note selectedNoteContext;

    private NotesAdapter adapter;

    private ProgressBar progressBar;

    private Button tryAgainButton;

    private int nameCount = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        presenter = new NotesListPresenter(this, new FireStoreNotesRepository());

        adapter = new NotesAdapter(this);
        adapter.setNoteClicked(new NotesAdapter.OnNoteClicked() {
            @Override
            public void onNoteClicked(Note note) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ARG_NOTE, note);

                getParentFragmentManager().setFragmentResult(KEY_NOTES_LIST_ACTIVITY, bundle);
            }

            @Override
            public void onNoteLongClicked(View itemView, Note note) {
                itemView.showContextMenu();
                selectedNoteContext = note;
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

        getParentFragmentManager().setFragmentResultListener(EditNoteFragment.KEY_RESULT, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                String title = result.getString(EditNoteFragment.ARG_TITLE);
                String content = result.getString(EditNoteFragment.ARG_CONTENT);
                String id = result.getString(EditNoteFragment.ARG_ID);

                presenter.update(id, title, content, selectedNoteContext);

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
    public void clearNotes() {
        adapter.setNotes(Collections.emptyList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addNote(Note result) {
        adapter.addNote(result);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
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

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem clear = menu.findItem(R.id.action_clear);
        clear.setVisible(true);
        clear.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                presenter.removeAll();
                nameCount = 0;
                return false;
            }
        });
        MenuItem add = menu.findItem(R.id.action_add);
        add.setVisible(true);
        add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                nameCount++;
                presenter.add("New note " + nameCount, "Note content");
                return false;
            }
        });
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        requireActivity().getMenuInflater().inflate(R.menu.notes_list_menu_comtext, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_note_context) {
            EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(selectedNoteContext);

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

            Toast.makeText(requireContext(), "Edit " + selectedNoteContext.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.delete_note_context) {
            presenter.delete(selectedNoteContext);
            Toast.makeText(requireContext(), "Delete " + selectedNoteContext.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void deleteNote(Note selectedNoteContext) {
        int notePosition = adapter.deleteNote(selectedNoteContext);

        adapter.notifyItemRemoved(notePosition);
    }

    @Override
    public void updateNote(Note result) {
        int notePosition = adapter.updateNote(result);

        adapter.notifyItemChanged(notePosition);
    }
}