package com.example.mynotesapp.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.mynotesapp.R;

import java.util.Calendar;
import java.util.Objects;

public class StartScreenFragment extends Fragment implements View.OnClickListener {

    private boolean isLand;

    private Fragment notesFr;

    private FragmentManager fragmentManager;

    private static final String CHANNEL_ID = "CHANNEL_ID";

    private int selectedYear = Calendar.getInstance().get(Calendar.YEAR);;
    private int selectedMonth = Calendar.getInstance().get(Calendar.MONTH);;
    private int selectedDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);;

    private DatePickerDialog datePickerDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button showNotesList = view.findViewById(R.id.view_list_notes);
        Button settings = view.findViewById(R.id.settings);
        Button aboutApp = view.findViewById(R.id.about_app);
        Button showNotification = view.findViewById(R.id.show_notification);
        Button showCalendar = view.findViewById(R.id.show_calendar);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            }
        };

        datePickerDialog = new DatePickerDialog(this.getContext(), dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

        showNotesList.setOnClickListener(this);
        settings.setOnClickListener(view1 -> showBottomSheet());
        aboutApp.setOnClickListener(this);
        showNotification.setOnClickListener(view1 -> showNotification());
        showCalendar.setOnClickListener(view1 -> showCalendar());

        notesFr = new NotesFragment();

        fragmentManager = getParentFragmentManager();

        isLand = getResources().getBoolean(R.bool.is_landscape);

        createNotificationChannel();
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

    private void showBottomSheet() {
        new MyBottomSheetFragment().show(getParentFragmentManager(), MyBottomSheetFragment.TAG);
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat.from(requireActivity()).notify(22, builder.build());
    }

    private void createNotificationChannel() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireActivity());
        String name = "Name";
        String descriptionText = "Description";
        int importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
        NotificationChannelCompat.Builder channel = new NotificationChannelCompat.Builder(CHANNEL_ID, importance);
        channel.setName(name)
                .setDescription(descriptionText);

        notificationManager.createNotificationChannel(channel.build());
    }

    private void showCalendar() {
        datePickerDialog.show();
    }
}