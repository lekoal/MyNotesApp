package com.example.mynotesapp.ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mynotesapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class AuthFragment extends Fragment {

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == Activity.RESULT_OK) {

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                if (task.isSuccessful()) {
                    NavHostFragment.findNavController(AuthFragment.this)
                            .navigate(R.id.action_authFragment_to_notesFragment);
                }
            }

        }
    });

    public AuthFragment() {
        super(R.layout.fragment_auth);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
            NavHostFragment.findNavController(AuthFragment.this)
                    .navigate(R.id.action_authFragment_to_notesFragment);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);

        googleSignInClient.getSignInIntent();

        view.findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInLauncher.launch(googleSignInClient.getSignInIntent());

            }
        });
    }
}
