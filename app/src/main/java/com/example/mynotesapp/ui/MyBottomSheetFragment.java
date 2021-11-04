package com.example.mynotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mynotesapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class MyBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "MyBottomSheetFragment";
    public static final String EXCHANGE_DATA_TAG = "EXCHANGE_DATA_TAG";
    public static final String TEXT_RESULT = "TEXT_RESULT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View customView = inflater.inflate(R.layout.fragment_my_bottom_sheet, null);

        customView.findViewById(R.id.bottom_sheet_button).setOnClickListener(view -> {
            String text = customView.<EditText>findViewById(R.id.bottom_sheet_edit_text).getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString(TEXT_RESULT, text);
            getParentFragmentManager().setFragmentResult(EXCHANGE_DATA_TAG, bundle);
            dismiss();
        });

        return customView;
    }
}