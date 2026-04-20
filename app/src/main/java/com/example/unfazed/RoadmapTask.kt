package com.example.unfazed

data class RoadmapTask(
    val id: Int,
    val title: String,
    val description: String,
    val semesterTarget: String,
    var status: TaskStatus,
    val actionText: String?,
    val actionType: ActionType?,
    val actionData: Map<String, String>? = null,
    val timeline: String? = null,
    val estimatedHours: Int? = null,
    val resources: List<String>? = null
)

enum class TaskStatus {
    LOCKED,
    ACTIVE,
    COMPLETED
}

enum class ActionType {
    OPEN_RESOURCES,
    OPEN_OPPORTUNITIES,
    OPEN_CHATBOT,
    OPEN_SEMESTER_GUIDE
}