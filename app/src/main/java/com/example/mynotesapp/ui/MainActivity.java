package com.example.mynotesapp.ui;

// Serializable - это стандартный интерфейс Java. Вы просто отмечаете класс Serializable, реализуя интерфейс, и Java будет автоматически сериализовать его в определенных ситуациях.
//
// Parcelable - это особый интерфейс для Android, где вы сами реализуете сериализацию. Он был создан гораздо эффективнее, чем Serializable, и чтобы обойти некоторые проблемы со схемой сериализации Java по умолчанию.
//
// По сравнению с Serializable, создание Parcelable объекта требует большого количества шаблонного кода, особенно для разработчиков java. Но в Serializable используется отражение, и в процессе будет создано много временных объектов. Таким образом, будет использовано много памяти. С другой стороны, Parcelable быстрее иболее оптимизирован, чем Serializable, потому что ответственность за создание объекта parcel лежит на разработчике, поэтому нет необходимости использовать отражение.

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mynotesapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final Fragment stFr = new StartScreenFragment();
    private final Fragment notesFr = new NotesFragment();
    private final Fragment aboutFr = new AboutFragment();

    private final int backColorRed = 1;
    private final int backColorBlue = 2;
    private final int backColorGreen = 3;
    private final int backColorBlack = 4;
    private final int backColorWhite = 5;

    private final int backLandColorRed = 6;
    private final int backLandColorBlue = 7;
    private final int backLandColorGreen = 8;
    private final int backLandColorBlack = 9;
    private final int backLandColorWhite = 10;

    private View textColor;
    private View textColorLand;

    private boolean isLand;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        textColor = findViewById(R.id.fragment_container);
        textColorLand = findViewById(R.id.fragment_container_right);

        getSupportFragmentManager().setFragmentResultListener(MyBottomSheetFragment.EXCHANGE_DATA_TAG, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String textResult = result.getString(MyBottomSheetFragment.TEXT_RESULT);
                Toast.makeText(MainActivity.this, "Entered text: " + textResult, Toast.LENGTH_SHORT).show();
            }
        });

        if (isLand) {
            registerForContextMenu(textColorLand);
        } else {
            registerForContextMenu(textColor);
        }

        isLand = getResources().getBoolean(R.bool.is_landscape);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_about) {
                    openAboutFragment();
                    return true;
                } else if (id == R.id.action_exit) {
                    showAlertOnExitDialog();
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, stFr)
                    .commit();
        }

        initToolbarAndDrawer();

    }

    private void initToolbarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.drawer_about_app) {
                    openAboutFragment();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.action_exit) {
                    showAlertOnExitDialog();
                    return true;
                } else if (id == R.id.drawer_note_list) {
                    openNoteList();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.start_page) {
                    openHomePage();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.drawer_settings) {
                    Toast.makeText(getApplicationContext(), "settings button is pressed!", Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });
    }

    private void openHomePage() {
        if (isLand) {
            removeInSecondContIfNotEmpty();
        }
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, stFr)
                .commit();

    }

    private void openAboutFragment() {
        if (isLand) {
            removeInSecondContIfNotEmpty();
        }
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, aboutFr)
                .commit();
    }

    private void openNoteList() {
        if (isLand) {
            removeInPrimContIfNotEmpty();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_left, notesFr)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, notesFr)
                    .commit();
        }
    }

    private void removeInPrimContIfNotEmpty() {
        if (fragmentManager.findFragmentById(R.id.fragment_container) != null) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .remove(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragment_container)))
                    .commit();
        }
    }

    private void removeInSecondContIfNotEmpty() {
        if (fragmentManager.findFragmentById(R.id.fragment_container_left) != null) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .remove(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragment_container_left)))
                    .commit();
        }

        if (fragmentManager.findFragmentById(R.id.fragment_container_right) != null) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .remove(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragment_container_right)))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // В landscape фрагменты накладываются друг на друга, поэтому пришлось заняться "жонглированием"
        int id = item.getItemId();
        if (id == R.id.action_about) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, aboutFr)
                    .commit();
            return true;
        } else if (id == R.id.action_exit) {
            showAlertOnExitDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isLand) {
            if (fragmentManager.findFragmentById(R.id.fragment_container) == null &&
                    fragmentManager.findFragmentById(R.id.fragment_container_right) == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, stFr)
                        .commit();
            }
        }
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null &&
                getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName().equals("StartScreenFragment")) {
            showAlertOnExitDialog();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.fragment_container:
                menu.add(0, backColorRed, 0, "Red");
                menu.add(0, backColorBlue, 0, "Blue");
                menu.add(0, backColorGreen, 0, "Green");
                menu.add(0, backColorBlack, 0, "Black");
                menu.add(0, backColorWhite, 0, "White");
                break;
            case R.id.fragment_container_right:
                menu.add(0, backLandColorRed, 0, "Red");
                menu.add(0, backLandColorBlue, 0, "Blue");
                menu.add(0, backLandColorGreen, 0, "Green");
                menu.add(0, backLandColorBlack, 0, "Black");
                menu.add(0, backLandColorWhite, 0, "White");
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case backColorRed:
                textColor.setBackgroundColor(Color.RED);
                break;
            case backColorBlue:
                textColor.setBackgroundColor(Color.BLUE);
                break;
            case backColorGreen:
                textColor.setBackgroundColor(Color.GREEN);
                break;
            case backColorBlack:
                textColor.setBackgroundColor(Color.BLACK);
                break;
            case backColorWhite:
                textColor.setBackgroundColor(Color.WHITE);
                break;
            case backLandColorRed:
                textColorLand.setBackgroundColor(Color.RED);
                break;
            case backLandColorBlue:
                textColorLand.setBackgroundColor(Color.BLUE);
                break;
            case backLandColorGreen:
                textColorLand.setBackgroundColor(Color.GREEN);
                break;
            case backLandColorBlack:
                textColorLand.setBackgroundColor(Color.BLACK);
                break;
            case backLandColorWhite:
                textColorLand.setBackgroundColor(Color.WHITE);
                break;
        }
        return super.onContextItemSelected(item);
    }

//    public void onDialogResult(String text) {
//        Toast.makeText(MainActivity.this, "Entered text: " + text, Toast.LENGTH_SHORT).show();
//    }

    public void showAlertOnExitDialog() {
        new AlertOnExitFragment().show(getSupportFragmentManager(), AlertOnExitFragment.TAG);
    }
}