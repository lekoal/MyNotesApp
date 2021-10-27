package com.example.mynotesapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mynotesapp.R;

public class AlertOnExitFragment extends DialogFragment {

    public static final String TAG = "AlertOnExitFragment";

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Activity activity = requireActivity();
        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setTitle("Alert!");
        dialog.setMessage("Are you sure wanna to exit?");
        dialog.setIcon(R.drawable.ic_baseline_warning_24);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(activity, "Exiting application...", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialogInterface, i) ->
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show());
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_alert_on_exit, container, false);
    }
}