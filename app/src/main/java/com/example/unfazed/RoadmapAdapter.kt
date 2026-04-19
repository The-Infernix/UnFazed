package com.example.unfazed

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class RoadmapAdapter(
    private val taskList: List<RoadmapTask>,
    private val onActionClicked: (ActionType) -> Unit // Handles button clicks
) : RecyclerView.Adapter<RoadmapAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSemesterTag: TextView = itemView.findViewById(R.id.tvSemesterTag)
        val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvTaskDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val btnTaskAction: MaterialButton = itemView.findViewById(R.id.btnTaskAction)
        val statusDot: View = itemView.findViewById(R.id.statusDot)
        val taskCard: MaterialCardView = itemView.findViewById(R.id.taskCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_roadmap_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        // 1. Set the basic text
        holder.tvSemesterTag.text = task.semesterTarget
        holder.tvTaskTitle.text = task.title
        holder.tvTaskDescription.text = task.description

        // 2. Handle the visual status (Locked vs Active/Completed)
        when (task.status) {
            TaskStatus.LOCKED -> {
                holder.taskCard.alpha = 0.5f // Dim the card if it's in the future
                holder.statusDot.setBackgroundColor(Color.parseColor("#BDBDBD")) // Grey dot
            }
            TaskStatus.ACTIVE -> {
                holder.taskCard.alpha = 1.0f
                holder.taskCard.strokeColor = Color.parseColor("#2196F3") // Blue border
                holder.statusDot.setBackgroundColor(Color.parseColor("#2196F3")) // Blue dot
            }
            TaskStatus.COMPLETED -> {
                holder.taskCard.alpha = 1.0f
                holder.taskCard.strokeColor = Color.parseColor("#4CAF50") // Green border
                holder.statusDot.setBackgroundColor(Color.parseColor("#4CAF50")) // Green dot
            }
        }

        // 3. Handle the Action Button
        if (task.actionText != null && task.actionType != null && task.status != TaskStatus.LOCKED) {
            holder.btnTaskAction.visibility = View.VISIBLE
            holder.btnTaskAction.text = task.actionText

            // When the user clicks the button, tell the Activity!
            holder.btnTaskAction.setOnClickListener {
                onActionClicked(task.actionType)
            }
        } else {
            holder.btnTaskAction.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}