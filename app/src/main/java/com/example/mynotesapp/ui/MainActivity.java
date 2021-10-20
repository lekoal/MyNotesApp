package com.example.mynotesapp.ui;

// requireActivity() возвращает Activity фрагмента, с которым в данный момент связан этот фрагмент. Метод, который возвращает ненулевой экземпляр Activity, в которой располагается  фрагмент, или создает исключение. Если вы на 100 % уверены, что в жизненном цикле вашего фрагмента Activity не равна нулю, используйте requireActivity(),  в противном случае поместите его в блок try-catch, чтобы избежать исключения NullPointerException.
//
// requireContext() возвращает контекст, с которым в данный момент связан этот фрагмент. Возвращает ненулевой контекст или создает исключение, когда он недоступен. Если ваш код находится на этапе жизненного цикла, когда вы знаете, что ваш фрагмент прикреплен к контексту, просто используйте requireContext(), чтобы получить контекст.
//
// getActivity() возвращает Activity, с которым в данный момент связан этот фрагмент. Может возвращать значение null, если фрагмент вместо этого связан с контекстом. Возвращает объект действия, к которому прикреплен фрагмент. Причина, по которой getActivity() во фрагменте не рекомендуется, заключается в следующем: этот метод вернет Activity, прикрепленное к текущему фрагменту. Когда жизненный цикл фрагмента заканчивается и уничтожается, getActivity() возвращает значение null, поэтому необходимо обрабатывать нулевые случаи, которые могут возникнуть при использовании getActivity().
//
// getContext() возвращает контекст, с которым в данный момент связан этот фрагмент. Это метод в классе View, доступ к которому возможен только в классе, который наследуется от класса View, и возвращает контекст Activity, в котором выполняется текущее View. Возвращает контекст, допускающий значение null. Если ваш код выходит за рамки обычного жизненного цикла фрагмента (скажем, асинхронный обратный вызов), вам может быть лучше использовать getContext(), самостоятельно проверяя его возвращаемое значение и продолжая использовать его только в том случае, если оно не равно нулю.

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.os.Bundle;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

public class MainActivity extends AppCompatActivity {

    private static final String ARG_NOTE = "ARG_NOTE";

    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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