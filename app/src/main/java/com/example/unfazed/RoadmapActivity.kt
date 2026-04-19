package com.example.unfazed

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RoadmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roadmap)

        // 1. Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Action Plan"

        // 2. Retrieve student profile from Intent
        val name = intent.getStringExtra("name") ?: "Student"
        val branch = intent.getStringExtra("branch") ?: "your branch"
        val goal = intent.getStringExtra("goal") ?: "Placement"
        val year = intent.getStringExtra("year") ?: "1st Year"

        val tvGoalTitle = findViewById<TextView>(R.id.tvGoalTitle)
        tvGoalTitle.text = "$name's Path to $goal"

        // 3. Generate the structured data based on user profile
        val taskList = generateDynamicTasks(branch, goal, year)

        // 4. Setup the RecyclerView & Adapter
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewRoadmap)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = RoadmapAdapter(taskList) { actionType ->
            handleActionClick(actionType)
        }
        recyclerView.adapter = adapter
    }

    // 🚀 This function decides WHERE the button takes the student!
    private fun handleActionClick(actionType: ActionType) {
        when (actionType) {
            ActionType.OPEN_RESOURCES -> {
                startActivity(Intent(this, CampusResourcesActivity::class.java))
            }
            ActionType.OPEN_OPPORTUNITIES -> {
                startActivity(Intent(this, OpportunitiesActivity::class.java))
            }
            ActionType.OPEN_CHATBOT -> {
                startActivity(Intent(this, ChatbotActivity::class.java))
            }
        }
    }

    // 🧠 The "Brain": Generates specific tasks as objects
    private fun generateDynamicTasks(branch: String, goal: String, year: String): List<RoadmapTask> {
        val tasks = mutableListOf<RoadmapTask>()

        if (goal.contains("Placement")) {
            if (year == "1st Year" || year == "2nd Year") {
                tasks.add(RoadmapTask(1, "Master Core Fundamentals", "Learn Python/Java and DSA basics.", "Semester 1-3", TaskStatus.COMPLETED, null, null))
                tasks.add(RoadmapTask(2, "Practice Hands-on Coding", "Solve 50+ problems. Use the IT Labs or DigiFac for a quiet environment.", "Current Focus", TaskStatus.ACTIVE, "Book DigiFac Lab", ActionType.OPEN_RESOURCES))
                tasks.add(RoadmapTask(3, "Build Your First Project", "Create a simple Web or Android app to understand development.", "Next Semester", TaskStatus.LOCKED, "Ask AI for Ideas", ActionType.OPEN_CHATBOT))
                tasks.add(RoadmapTask(4, "Join a Hackathon", "Apply your skills in real-time. Don't worry about winning, just participate!", "Year 2 End", TaskStatus.LOCKED, "Find Hackathons", ActionType.OPEN_OPPORTUNITIES))
            } else {
                tasks.add(RoadmapTask(1, "Build Advanced Projects", "Develop 2 strong projects for your resume using A-Hub resources.", "Current Focus", TaskStatus.ACTIVE, "Explore A-Hub", ActionType.OPEN_RESOURCES))
                tasks.add(RoadmapTask(2, "Secure Summer Internship", "Apply to at least 10 companies. Your resume needs real-world experience.", "Next Month", TaskStatus.ACTIVE, "View Internships", ActionType.OPEN_OPPORTUNITIES))
                tasks.add(RoadmapTask(3, "Mock Interviews", "Practice HR and Technical interviews with peers in the Central Library.", "Semester 7", TaskStatus.LOCKED, null, null))
            }
        }
        else if (goal.contains("Entrepreneurship") || goal.contains("Startup")) {
            tasks.add(RoadmapTask(1, "Find a Problem to Solve", "Observe campus and society. What is broken? Write it down.", "Current", TaskStatus.COMPLETED, null, null))
            tasks.add(RoadmapTask(2, "Learn Business Basics", "Attend weekend workshops at A-Hub to learn about MVPs and pitching.", "Current Focus", TaskStatus.ACTIVE, "Check A-Hub Events", ActionType.OPEN_RESOURCES))
            tasks.add(RoadmapTask(3, "Build an MVP", "Create a prototype of your idea using DigiFac equipment.", "Next Semester", TaskStatus.LOCKED, "View Labs", ActionType.OPEN_RESOURCES))
        }
        else {
            // Default generic roadmap for GATE/Higher Studies
            tasks.add(RoadmapTask(1, "Analyze the Syllabus", "Download the official syllabus for $branch.", "Current", TaskStatus.ACTIVE, "Ask AI Chatbot", ActionType.OPEN_CHATBOT))
            tasks.add(RoadmapTask(2, "Collect Reference Books", "Visit the AU E-Library and issue standard textbooks.", "Next Week", TaskStatus.LOCKED, "Open E-Library", ActionType.OPEN_RESOURCES))
            tasks.add(RoadmapTask(3, "Start Mock Tests", "Test your knowledge under time constraints.", "Next Semester", TaskStatus.LOCKED, null, null))
        }

        return tasks
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}