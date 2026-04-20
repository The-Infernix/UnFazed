package com.example.unfazed

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class RoadmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roadmap)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Career Roadmap"

        val name = intent.getStringExtra("name") ?: "Student"
        val branch = intent.getStringExtra("branch") ?: "your branch"
        val goal = intent.getStringExtra("goal") ?: "Placement"
        val year = intent.getStringExtra("year") ?: "1st Year"

        val tvGoalTitle = findViewById<TextView>(R.id.tvGoalTitle)
        tvGoalTitle.text = "$name's $goal Roadmap"

        val taskList = generateSmartPlacementRoadmap(branch, goal, year)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewRoadmap)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RoadmapAdapter(taskList) { actionType, data ->
            handleActionClick(actionType, data)
        }
        recyclerView.adapter = adapter
    }

    private fun handleActionClick(actionType: ActionType, data: Map<String, String>?) {
        when (actionType) {
            ActionType.OPEN_RESOURCES -> {
                startActivity(Intent(this, CampusResourcesActivity::class.java))
            }
            ActionType.OPEN_OPPORTUNITIES -> {
                startActivity(Intent(this, OpportunitiesActivity::class.java))
            }
            ActionType.OPEN_CHATBOT -> {
                val intent = Intent(this, ChatbotActivity::class.java)
                data?.let {
                    intent.putExtra("topic", it["topic"])
                    intent.putExtra("context", it["context"])
                }
                startActivity(intent)
            }
            ActionType.OPEN_SEMESTER_GUIDE -> {
                startActivity(Intent(this, SemesterGuideActivity::class.java))
            }
        }
    }

    private fun generateSmartPlacementRoadmap(branch: String, goal: String, year: String): List<RoadmapTask> {
        val tasks = mutableListOf<RoadmapTask>()
        val currentYear = extractYearNumber(year)

        when {
            goal.contains("Placement") || goal.contains("Job") -> {
                // PHASE 1: Foundation (Year 1-2)
                if (currentYear <= 2) {
                    tasks.addAll(getFoundationPhase(branch))
                }

                // PHASE 2: Skill Building (Year 2-3)
                if (currentYear <= 3) {
                    tasks.addAll(getSkillBuildingPhase(branch))
                }

                // PHASE 3: Advanced Preparation (Year 3-4)
                if (currentYear >= 3) {
                    tasks.addAll(getAdvancedPhase(branch))
                }

                // PHASE 4: Placement Season (Year 4)
                if (currentYear >= 4) {
                    tasks.addAll(getPlacementPhase(branch))
                }
            }
            goal.contains("GATE") -> {
                tasks.addAll(getGATERoadmap(branch, currentYear))
            }
            else -> {
                tasks.addAll(getGeneralRoadmap(branch))
            }
        }

        return tasks
    }

    private fun getFoundationPhase(branch: String): List<RoadmapTask> {
        return listOf(
            RoadmapTask(
                id = 1,
                title = "🎯 Programming Fundamentals",
                description = if (branch.contains("CSE"))
                    "Master Python/Java fundamentals. Understand OOP concepts, data types, and basic algorithms."
                else "Learn basics of programming in C/Python. Understand logic building and problem-solving.",
                semesterTarget = "Year 1 - Semester 1/2",
                status = TaskStatus.ACTIVE,
                actionText = "Start Learning",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "Programming Fundamentals", "context" to "Beginner programming guide"),
                timeline = "Month 1-3",
                estimatedHours = 120,
                resources = listOf("NPTEL", "Coursera", "YouTube")
            ),
            RoadmapTask(
                id = 2,
                title = "📊 Data Structures & Algorithms",
                description = "Learn Arrays, Linked Lists, Stacks, Queues, Trees, Graphs. Master time complexity analysis.",
                semesterTarget = "Year 1 - Semester 2",
                status = TaskStatus.LOCKED,
                actionText = "Get Study Plan",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "DSA Roadmap", "context" to "Complete DSA preparation guide"),
                timeline = "Month 4-6",
                estimatedHours = 200,
                resources = listOf("LeetCode", "GeeksforGeeks", "CodeChef")
            ),
            RoadmapTask(
                id = 3,
                title = "💻 Core Computer Science Subjects",
                description = "Master Operating Systems, DBMS, Computer Networks, and OOP concepts.",
                semesterTarget = "Year 2 - Semester 3",
                status = TaskStatus.LOCKED,
                actionText = "Access Resources",
                actionType = ActionType.OPEN_RESOURCES,
                actionData = mapOf("type" to "core_subjects"),
                timeline = "Month 7-9",
                estimatedHours = 180,
                resources = listOf("Standard Textbooks", "NPTEL", "Campus Library")
            )
        )
    }

    private fun getSkillBuildingPhase(branch: String): List<RoadmapTask> {
        return listOf(
            RoadmapTask(
                id = 4,
                title = "🚀 Build Projects Portfolio",
                description = if (branch.contains("CSE"))
                    "Build 2-3 full-stack projects using MERN/MEAN stack. Deploy on cloud platforms."
                else "Build industry-relevant projects using core engineering tools and technologies.",
                semesterTarget = "Year 2 - Semester 4",
                status = TaskStatus.LOCKED,
                actionText = "Get Project Ideas",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "Project Ideas", "context" to "$branch project suggestions"),
                timeline = "Month 10-12",
                estimatedHours = 250,
                resources = listOf("GitHub", "A-Hub Lab", "Project Lab")
            ),
            RoadmapTask(
                id = 5,
                title = "🏆 Competitive Programming",
                description = "Solve 200+ problems on LeetCode/HackerRank. Participate in weekly contests.",
                semesterTarget = "Year 2 - Semester 4",
                status = TaskStatus.LOCKED,
                actionText = "Start Practicing",
                actionType = ActionType.OPEN_OPPORTUNITIES,
                actionData = mapOf("type" to "coding_contests"),
                timeline = "Ongoing",
                estimatedHours = 150,
                resources = listOf("LeetCode", "Codeforces", "CodeChef")
            ),
            RoadmapTask(
                id = 6,
                title = "📝 Aptitude & Reasoning",
                description = "Master quantitative aptitude, logical reasoning, and verbal ability.",
                semesterTarget = "Year 3 - Semester 5",
                status = TaskStatus.LOCKED,
                actionText = "Practice Tests",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "Aptitude Preparation", "context" to "Company-specific aptitude"),
                timeline = "Month 13-15",
                estimatedHours = 100,
                resources = listOf("Indiabix", "Face Prep", "Previous Papers")
            )
        )
    }

    private fun getAdvancedPhase(branch: String): List<RoadmapTask> {
        return listOf(
            RoadmapTask(
                id = 7,
                title = "🎓 Resume Building & LinkedIn Optimization",
                description = "Create ATS-friendly resume. Optimize LinkedIn profile. Build strong portfolio/GitHub.",
                semesterTarget = "Year 3 - Semester 5",
                status = TaskStatus.LOCKED,
                actionText = "Get Template",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "Resume Tips", "context" to "$branch resume template"),
                timeline = "Month 16",
                estimatedHours = 40,
                resources = listOf("Career Center", "Online Templates")
            ),
            RoadmapTask(
                id = 8,
                title = "💼 Internship Applications",
                description = "Apply to 20+ companies for summer internship. Prepare for internship interviews.",
                semesterTarget = "Year 3 - Semester 6",
                status = TaskStatus.LOCKED,
                actionText = "Find Internships",
                actionType = ActionType.OPEN_OPPORTUNITIES,
                actionData = mapOf("type" to "internships"),
                timeline = "Month 17-18",
                estimatedHours = 80,
                resources = listOf("Internshala", "LinkedIn", "Company Websites")
            ),
            RoadmapTask(
                id = 9,
                title = "🎯 Company-Specific Preparation",
                description = "Research target companies. Practice company-specific questions and mock tests.",
                semesterTarget = "Year 3 - Semester 6",
                status = TaskStatus.LOCKED,
                actionText = "View Companies",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "Company Prep", "context" to "Top companies guide"),
                timeline = "Month 19-21",
                estimatedHours = 120,
                resources = listOf("Glassdoor", "LeetCode Discuss", "YouTube")
            )
        )
    }

    private fun getPlacementPhase(branch: String): List<RoadmapTask> {
        return listOf(
            RoadmapTask(
                id = 10,
                title = "⚡ Mock Interview Practice",
                description = "Take 10+ mock interviews. Practice technical, HR, and managerial rounds.",
                semesterTarget = "Year 4 - Semester 7",
                status = TaskStatus.LOCKED,
                actionText = "Start Practice",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "Mock Interviews", "context" to "Interview preparation"),
                timeline = "Month 22-24",
                estimatedHours = 60,
                resources = listOf("Pramp", "InterviewBit", "College Seniors")
            ),
            RoadmapTask(
                id = 11,
                title = "📢 Placement Drive Preparation",
                description = "Register for placement drives. Prepare for group discussions and aptitude tests.",
                semesterTarget = "Year 4 - Semester 7",
                status = TaskStatus.LOCKED,
                actionText = "Check Schedule",
                actionType = ActionType.OPEN_OPPORTUNITIES,
                actionData = mapOf("type" to "placement_drives"),
                timeline = "Month 25-27",
                estimatedHours = 50,
                resources = listOf("Placement Cell", "Training Center")
            ),
            RoadmapTask(
                id = 12,
                title = "🎉 Placement Success!",
                description = "Apply to dream companies. Ace interviews. Get your offer letter!",
                semesterTarget = "Year 4 - Semester 8",
                status = TaskStatus.LOCKED,
                actionText = "Get Tips",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "Placement Tips", "context" to "Final preparation"),
                timeline = "Month 28-30",
                estimatedHours = 40,
                resources = listOf("Career Center", "Alumni Network")
            )
        )
    }

    private fun getGATERoadmap(branch: String, currentYear: Int): List<RoadmapTask> {
        return listOf(
            RoadmapTask(
                id = 1,
                title = "📚 Syllabus Completion",
                description = "Complete 100% of GATE syllabus for $branch. Focus on high-weightage topics.",
                semesterTarget = "Year 1-3",
                status = if (currentYear <= 2) TaskStatus.ACTIVE else TaskStatus.COMPLETED,
                actionText = "View Syllabus",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "GATE Syllabus", "context" to branch),
                timeline = "Months 1-18",
                estimatedHours = 400,
                resources = listOf("Standard Textbooks", "NPTEL", "Previous Papers")
            ),
            RoadmapTask(
                id = 2,
                title = "✍️ PYQs Practice",
                description = "Solve previous 10 years' question papers. Analyze patterns and important topics.",
                semesterTarget = "Year 3",
                status = if (currentYear == 3) TaskStatus.ACTIVE else TaskStatus.LOCKED,
                actionText = "Get PYQs",
                actionType = ActionType.OPEN_RESOURCES,
                actionData = mapOf("type" to "pyqs"),
                timeline = "Months 19-24",
                estimatedHours = 200,
                resources = listOf("Made Easy", "ACE Academy", "GATE Overflow")
            ),
            RoadmapTask(
                id = 3,
                title = "📊 Mock Tests & Revision",
                description = "Take 20+ full-length mock tests. Revise weak areas thoroughly.",
                semesterTarget = "Year 4",
                status = if (currentYear >= 4) TaskStatus.ACTIVE else TaskStatus.LOCKED,
                actionText = "Start Tests",
                actionType = ActionType.OPEN_OPPORTUNITIES,
                actionData = mapOf("type" to "mock_tests"),
                timeline = "Months 25-30",
                estimatedHours = 150,
                resources = listOf("Test Series", "Online Platforms")
            )
        )
    }

    private fun getGeneralRoadmap(branch: String): List<RoadmapTask> {
        return listOf(
            RoadmapTask(
                id = 1,
                title = "🎓 Academic Excellence",
                description = "Maintain CGPA above 8.0. Focus on core subjects.",
                semesterTarget = "All Semesters",
                status = TaskStatus.ACTIVE,
                actionText = "Study Tips",
                actionType = ActionType.OPEN_SEMESTER_GUIDE,
                actionData = null,
                timeline = "Ongoing",
                estimatedHours = null,
                resources = null
            ),
            RoadmapTask(
                id = 2,
                title = "🔍 Explore Career Options",
                description = "Research different career paths in $branch.",
                semesterTarget = "Year 2-3",
                status = TaskStatus.LOCKED,
                actionText = "Explore",
                actionType = ActionType.OPEN_CHATBOT,
                actionData = mapOf("topic" to "Career Options", "context" to branch),
                timeline = "Month 6-12",
                estimatedHours = 50,
                resources = listOf("Career Center", "Alumni")
            )
        )
    }

    private fun extractYearNumber(year: String): Int {
        return when {
            year.contains("1st") -> 1
            year.contains("2nd") -> 2
            year.contains("3rd") -> 3
            year.contains("4th") -> 4
            else -> 1
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}