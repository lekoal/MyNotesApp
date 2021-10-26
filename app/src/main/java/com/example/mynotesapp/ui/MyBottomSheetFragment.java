package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mynotesapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class MyBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "MyBottomSheetFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View customView = inflater.inflate(R.layout.fragment_my_bottom_sheet, null);

        customView.findViewById(R.id.bottom_sheet_button).setOnClickListener(view -> {
            String text = customView.<EditText>findViewById(R.id.bottom_sheet_edit_text).getText().toString();
            ((MainActivity) requireActivity()).onDialogResult(text);
            dismiss();
        });

        return customView;
    }
}