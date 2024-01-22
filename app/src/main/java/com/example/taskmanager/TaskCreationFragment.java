package com.example.taskmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class TaskCreationFragment extends Fragment {

    public TaskCreationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_creation, container, false);

        final EditText taskTitleEditText = view.findViewById(R.id.editTextTaskTitle);
        final EditText taskDescriptionEditText = view.findViewById(R.id.editTextTaskDescription);
        Button saveTaskButton = view.findViewById(R.id.buttonSaveTask);

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = taskTitleEditText.getText().toString().trim();
                String description = taskDescriptionEditText.getText().toString().trim();

                Task newTask = new Task(title, description);


                saveTaskToFirestore(newTask);
            }
        });

        return view;
    }

    private void saveTaskToFirestore(Task task) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid(); // Get the current user's ID
            task.setUserId(userId); // Set the user ID in the task

            // Add a new document with a generated ID
            db.collection("tasks")
                    .add(task)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getActivity(), "Task added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("TaskCreationFragment", "Error adding task", e);
                        Toast.makeText(getActivity(), "Error adding task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }



}
