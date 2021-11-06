package com.example.mynotesapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mynotesapp.R;
import com.example.mynotesapp.domain.Note;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final Fragment stFr = new StartScreenFragment();
    private final Fragment notesFr = new NotesFragment();
    private final Fragment aboutFr = new AboutFragment();

    private View textColor;
    private View textColorLand;

    private boolean isLand;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController navCo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        navCo = navHostFragment.getNavController();

        fragmentManager = getSupportFragmentManager();

        textColor = findViewById(R.id.fragment_container);
        textColorLand = findViewById(R.id.fragment_container_right);

        getSupportFragmentManager().setFragmentResultListener(MyBottomSheetFragment.EXCHANGE_DATA_TAG, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String textResult = result.getString(MyBottomSheetFragment.TEXT_RESULT);
                Toast.makeText(MainActivity.this, getString(R.string.entered_text_message) + textResult, Toast.LENGTH_SHORT).show();
            }
        });

        if (isLand) {
            registerForContextMenu(textColorLand);
        } else {
            registerForContextMenu(textColor);
        }

        isLand = getResources().getBoolean(R.bool.is_landscape);

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
                    Toast.makeText(getApplicationContext(), getString(R.string.settings_button_message), Toast.LENGTH_SHORT).show();
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

        navCo.navigate(R.id.action_startScreenFragment_to_authFragment);
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
        menu.findItem(R.id.action_clear).setVisible(false);
        menu.findItem(R.id.action_add).setVisible(false);
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
        if (Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getClass().isInstance(stFr)) {
            showAlertOnExitDialog();
        } else {
            super.onBackPressed();
        }
    }

    public void showAlertOnExitDialog() {
        new AlertOnExitFragment().show(getSupportFragmentManager(), AlertOnExitFragment.TAG);
    }
}