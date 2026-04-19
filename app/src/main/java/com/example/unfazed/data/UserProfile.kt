package com.example.unfazed.data

data class UserProfile(
    val name: String,
    val branch: String,
    val year: String,
    val goal: String,
    val interests: List<String> = emptyList()
)

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class RoadmapItem(
    val title: String,
    val steps: List<String>,
    val duration: String,
    val icon: String
)

data class CampusResource(
    val name: String,
    val type: String, // Lab, Library, Facility
    val description: String,
    val location: String,
    val accessInfo: String,
    val icon: Int
)

data class Opportunity(
    val title: String,
    val type: String, // Course, Internship, Hackathon
    val provider: String,
    val deadline: String,
    val link: String,
    val matchScore: Int
)