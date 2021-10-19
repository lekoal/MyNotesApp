package com.example.mynotesapp.ui;

// Serializable - это стандартный интерфейс Java. Вы просто отмечаете класс Serializable, реализуя интерфейс, и Java будет автоматически сериализовать его в определенных ситуациях.
//
// Parcelable - это особый интерфейс для Android, где вы сами реализуете сериализацию. Он был создан гораздо эффективнее, чем Serializable, и чтобы обойти некоторые проблемы со схемой сериализации Java по умолчанию.
//
// По сравнению с Serializable, создание Parcelable объекта требует большого количества шаблонного кода, особенно для разработчиков java. Но в Serializable используется отражение, и в процессе будет создано много временных объектов. Таким образом, будет использовано много памяти. С другой стороны, Parcelable быстрее иболее оптимизирован, чем Serializable, потому что ответственность за создание объекта parcel лежит на разработчике, поэтому нет необходимости использовать отражение.

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

public class MainActivity extends AppCompatActivity {

    private static final String ARG_NOTE = "ARG_NOTE";

    private Note selectedNote;

    StartScreenFragment startScreenFragment;

    FragmentTransaction fTr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startScreenFragment = new StartScreenFragment();
        fTr = getSupportFragmentManager().beginTransaction();
        fTr.replace(R.id.fragment_container, startScreenFragment);
        fTr.commit();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (!(fragmentManager.findFragmentById(R.id.fragment_container) instanceof NotesFragment)) {
            fragmentManager.popBackStack();
        }

        boolean isLandscape = getResources().getBoolean(R.bool.is_landscape);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_NOTE)) {
            selectedNote = savedInstanceState.getParcelable(ARG_NOTE);

            if (isLandscape) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ARG_NOTE, selectedNote);

                fragmentManager.setFragmentResult(NoteDetailsFragment.KEY_NOTES_LIST_DETAILS, bundle);
            } else {
                NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(selectedNote);

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

        getSupportFragmentManager().setFragmentResultListener(NotesFragment.KEY_NOTES_LIST_ACTIVITY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                selectedNote = result.getParcelable(NotesFragment.ARG_NOTE);

                if (isLandscape) {

                    fragmentManager.setFragmentResult(NoteDetailsFragment.KEY_NOTES_LIST_DETAILS, result);
                } else {
                    NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(selectedNote);

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, detailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (selectedNote != null) {
            outState.putParcelable(ARG_NOTE, selectedNote);
        }
        super.onSaveInstanceState(outState);
    }
}