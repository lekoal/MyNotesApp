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

import android.os.Bundle;
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

    private boolean isLand;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

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
                    finish();
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
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
                } else if (id == R.id.drawer_exit) {
                    finish();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, aboutFr)
                    .commit();
            return true;
        } else if (id == R.id.action_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}