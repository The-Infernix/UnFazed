package com.example.unfazed

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class RoadmapAdapter(
    private val taskList: List<RoadmapTask>,
    private val onActionClicked: (ActionType, Map<String, String>?) -> Unit
) : RecyclerView.Adapter<RoadmapAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSemesterTag: TextView = itemView.findViewById(R.id.tvSemesterTag)
        val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvTaskDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val btnTaskAction: MaterialButton = itemView.findViewById(R.id.btnTaskAction)
        val statusDot: View = itemView.findViewById(R.id.statusDot)
        val taskCard: MaterialCardView = itemView.findViewById(R.id.taskCard)
        val tvTimeline: TextView = itemView.findViewById(R.id.tvTimeline)
        val tvEstimatedHours: TextView = itemView.findViewById(R.id.tvEstimatedHours)
        val chipGroupResources: ChipGroup = itemView.findViewById(R.id.chipGroupResources)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val layoutExpanded: LinearLayout = itemView.findViewById(R.id.layoutExpanded)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_roadmap_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        // Basic info
        holder.tvSemesterTag.text = task.semesterTarget
        holder.tvTaskTitle.text = task.title
        holder.tvTaskDescription.text = task.description

        // Show timeline and hours if available
        if (!task.timeline.isNullOrEmpty()) {
            holder.tvTimeline.text = "⏰ ${task.timeline}"
            holder.tvTimeline.visibility = View.VISIBLE
        } else {
            holder.tvTimeline.visibility = View.GONE
        }

        if (task.estimatedHours != null && task.estimatedHours > 0) {
            holder.tvEstimatedHours.text = "📚 ${task.estimatedHours} hours estimated"
            holder.tvEstimatedHours.visibility = View.VISIBLE
        } else {
            holder.tvEstimatedHours.visibility = View.GONE
        }

        // Add resource chips
        if (task.resources != null && task.resources.isNotEmpty()) {
            holder.chipGroupResources.removeAllViews()
            task.resources.forEach { resource ->
                val chip = Chip(holder.itemView.context).apply {
                    text = resource
                    textSize = 11f
                    setChipBackgroundColorResource(android.R.color.transparent)
                    setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.primary))
                    // FIXED: strokeColor expects Int, not Color
                    chipStrokeColor = ContextCompat.getColorStateList(holder.itemView.context, R.color.primary)
                    // FIXED: strokeWidth expects Int (pixels), convert from dp
                    chipStrokeWidth = 1f  // This is in pixels, use 1f for 1px
                    isClickable = false
                    isCheckable = false
                }
                holder.chipGroupResources.addView(chip)
            }
            holder.chipGroupResources.visibility = View.VISIBLE
        } else {
            holder.chipGroupResources.visibility = View.GONE
        }

        // Set progress based on status
        when (task.status) {
            TaskStatus.COMPLETED -> {
                holder.progressBar.progress = 100
                holder.progressBar.visibility = View.VISIBLE
            }
            TaskStatus.ACTIVE -> {
                holder.progressBar.progress = 50
                holder.progressBar.visibility = View.VISIBLE
            }
            TaskStatus.LOCKED -> {
                holder.progressBar.progress = 0
                holder.progressBar.visibility = View.GONE
            }
        }

        // Set visual styling based on status
        when (task.status) {
            TaskStatus.LOCKED -> {
                holder.taskCard.alpha = 0.6f
                holder.statusDot.setBackgroundColor(Color.parseColor("#BDBDBD"))
                holder.layoutExpanded.visibility = View.GONE
            }
            TaskStatus.ACTIVE -> {
                holder.taskCard.alpha = 1.0f
                holder.taskCard.strokeColor = Color.parseColor("#6366F1")
                holder.statusDot.setBackgroundColor(Color.parseColor("#6366F1"))
                holder.layoutExpanded.visibility = View.VISIBLE
            }
            TaskStatus.COMPLETED -> {
                holder.taskCard.alpha = 1.0f
                holder.taskCard.strokeColor = Color.parseColor("#10B981")
                holder.statusDot.setBackgroundColor(Color.parseColor("#10B981"))
                holder.layoutExpanded.visibility = View.VISIBLE
            }
        }

        // Handle action button
        if (task.actionText != null && task.actionType != null && task.status != TaskStatus.LOCKED) {
            holder.btnTaskAction.visibility = View.VISIBLE
            holder.btnTaskAction.text = task.actionText
            holder.btnTaskAction.setOnClickListener {
                onActionClicked(task.actionType, task.actionData)
            }
        } else {
            holder.btnTaskAction.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = taskList.size
}