package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<String> tasks;

    @Override
    protected void onResume() {
        super.onResume();
        fetchTasks();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);


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

        recyclerView = findViewById(R.id.tasks_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        adapter = new TaskAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fetchTasks();
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int spacing = getResources().getDimensionPixelSize(R.dimen.recycler_spacing); // Użyj wartości z resources

                int halfSpacing = spacing / 2;
                outRect.left = halfSpacing;
                outRect.right = halfSpacing;
                outRect.top = halfSpacing;
                outRect.bottom = halfSpacing;

                // Usuwamy dodatkowy odstęp na górze pierwszego wiersza i na dole ostatniego
                if (parent.getChildLayoutPosition(view) < 2) {
                    outRect.top = spacing;
                }

                int childCount = state.getItemCount();
                if (parent.getChildLayoutPosition(view) >= childCount - 2) {
                    outRect.bottom = spacing;
                }
            }
        });


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
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_bottom_item2) {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        }
        else if (itemId == R.id.nav_bottom_item3) {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        }
        else if (itemId == R.id.nav_bottom_item4) {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
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

    private void fetchTasks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            db.collection("tasks")
                    .whereEqualTo("userId", currentUserId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Task> tasksList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Task taskItem = document.toObject(Task.class);
                                tasksList.add(taskItem);
                            }
                            adapter = new TaskAdapter(tasksList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("MainActivity", "Error getting documents: ", task.getException());
                            Toast.makeText(MainActivity.this, "Error fetching tasks", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }




    private void checkFirebaseConnection() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean connected = snapshot.getValue(Boolean.class);
                if (connected != null && connected) {
                    // Connected to the database
                    Toast.makeText(MainActivity.this, "Connected to Firebase Database", Toast.LENGTH_SHORT).show();
                } else {
                    // Not connected to the database
                    Toast.makeText(MainActivity.this, "Not connected to Firebase Database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Listener was cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleMenuItemClick(int itemId) {
        if (itemId == R.id.action_add_task) {
            Snackbar.make(fab, "Dodaj zadanie", Snackbar.LENGTH_SHORT).show();
        } else if (itemId == R.id.action_add_reminder) {
            Snackbar.make(fab, "Dodaj przypomnienie", Snackbar.LENGTH_SHORT).show();
        }
    }
}