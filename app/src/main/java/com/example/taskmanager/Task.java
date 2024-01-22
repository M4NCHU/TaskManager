package com.example.taskmanager;

public class Task {
    private String id; // A unique ID for the task
    private String title;
    private String description;
    private long timestamp;
    private String userId;

    // Default constructor is needed for Firebase
    public Task() {
    }

    // Constructor to use when creating the task object
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.timestamp = System.currentTimeMillis();

    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
