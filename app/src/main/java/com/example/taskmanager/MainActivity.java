package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_name);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        

        // Ustawienie nazwy aplikacji w Toolbar
        getSupportActionBar().setTitle("Nazwa aplikacji");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userNameTextView.setText(currentUser.getDisplayName());
        }


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleBottomNavigationItemSelected(item);
            }
        });
    }

    private boolean handleBottomNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_bottom_item1) {
            // Obsługa kliknięcia Item 1
        } else if (itemId == R.id.nav_bottom_item2) {
            // Obsługa kliknięcia Item 2
        }


        return true;
    }


    private void openFabMenu() {
        PopupMenu popupMenu = new PopupMenu(this, fab);
        popupMenu.getMenuInflater().inflate(R.menu.fab_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            handleMenuItemClick(item.getItemId());
            return true;
        });

        popupMenu.show();
    }

    private void handleMenuItemClick(int itemId) {
        if (itemId == R.id.action_add_task) {
            Snackbar.make(fab, "Dodaj zadanie", Snackbar.LENGTH_SHORT).show();
        } else if (itemId == R.id.action_add_reminder) {
            Snackbar.make(fab, "Dodaj przypomnienie", Snackbar.LENGTH_SHORT).show();
        }
    }
}