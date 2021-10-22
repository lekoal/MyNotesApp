package com.example.mynotesapp.ui;

// Serializable - это стандартный интерфейс Java. Вы просто отмечаете класс Serializable, реализуя интерфейс, и Java будет автоматически сериализовать его в определенных ситуациях.
//
// Parcelable - это особый интерфейс для Android, где вы сами реализуете сериализацию. Он был создан гораздо эффективнее, чем Serializable, и чтобы обойти некоторые проблемы со схемой сериализации Java по умолчанию.
//
// По сравнению с Serializable, создание Parcelable объекта требует большого количества шаблонного кода, особенно для разработчиков java. Но в Serializable используется отражение, и в процессе будет создано много временных объектов. Таким образом, будет использовано много памяти. С другой стороны, Parcelable быстрее иболее оптимизирован, чем Serializable, потому что ответственность за создание объекта parcel лежит на разработчике, поэтому нет необходимости использовать отражение.

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;

public class MainActivity extends AppCompatActivity {

    StartScreenFragment startScreenFragment;

    FragmentTransaction fTr;

    @Override
    public void onBackPressed() {
        if (startScreenFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            startScreenFragment.getChildFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }

        View startButtonContainer = findViewById(R.id.start_screen_button_container);
        if (startButtonContainer.getVisibility() == View.INVISIBLE &&
                startScreenFragment.getChildFragmentManager().findFragmentById(R.id.child_container) == null) { // Всем костылям костыль!
            startButtonContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startScreenFragment = new StartScreenFragment();
        fTr = getSupportFragmentManager().beginTransaction();
        fTr.replace(R.id.fragment_container, startScreenFragment);
        fTr.commit();

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, new AboutFragment())
                    .commit();
            return true;
        } else if (id == R.id.action_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}