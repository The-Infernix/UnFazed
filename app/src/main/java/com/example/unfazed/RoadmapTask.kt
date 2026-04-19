package com.example.unfazed

// 1. The Data Class
data class RoadmapTask(
    val id: Int,
    val title: String,
    val description: String,
    val semesterTarget: String,
    var status: TaskStatus,
    val actionText: String?,
    val actionType: ActionType?
)

// 2. The TaskStatus Enum
enum class TaskStatus {
    LOCKED,
    ACTIVE,
    COMPLETED
}

// 3. The ActionType Enum
enum class ActionType {
    OPEN_RESOURCES,
    OPEN_OPPORTUNITIES,
    OPEN_CHATBOT
}