package com.example.taskmanager;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    public TextView taskTitle;

    public TaskViewHolder(View itemView) {
        super(itemView);
        taskTitle = itemView.findViewById(R.id.task_title);
    }
}
