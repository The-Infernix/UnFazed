package com.example.unfazed

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DashboardActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var tvDetails: TextView
    private lateinit var tvTarget: TextView
    private lateinit var tvProgress: TextView

    private var name: String = ""
    private var branch: String = ""
    private var year: String = ""
    private var goal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Get user data from intent
        name = intent.getStringExtra("name") ?: "Student"
        branch = intent.getStringExtra("branch") ?: ""
        year = intent.getStringExtra("year") ?: ""
        goal = intent.getStringExtra("goal") ?: ""

        // Initialize views
        initViews()

        // Set user data
        setupUserData()

        // Setup click listeners
        setupClickListeners()

        // Calculate and show progress
        calculateProgress()
    }

    private fun initViews() {
        tvWelcome = findViewById(R.id.tvWelcome)
        tvDetails = findViewById(R.id.tvDetails)
        tvTarget = findViewById(R.id.tvTarget)
        tvProgress = findViewById(R.id.tvProgress)
    }

    private fun setupUserData() {
        tvWelcome.text = "Hey $name! 👋"
        tvDetails.text = "$branch • $year"
        tvTarget.text = when {
            goal.contains("Placement") -> "🎯 Placement Ready"
            goal.contains("GATE") -> "📚 GATE Warrior"
            goal.contains("Higher Studies") -> "🎓 Scholar Track"
            goal.contains("Entrepreneurship") -> "💼 Founder Path"
            else -> "🚀 Career Growth"
        }
    }

    private fun calculateProgress() {
        // Calculate progress based on year and goal
        val progress = when {
            year.contains("1st") -> 25
            year.contains("2nd") -> 50
            year.contains("3rd") -> 75
            year.contains("4th") -> 90
            else -> 0
        }
        tvProgress.text = "$progress%"
    }

    private fun setupClickListeners() {
        // Roadmap Button
        findViewById<MaterialButton>(R.id.btnRoadmap).setOnClickListener {
            val intent = Intent(this, RoadmapActivity::class.java).apply {
                putExtra("branch", branch)
                putExtra("goal", goal)
                putExtra("year", year)
                putExtra("name", name)
            }
            startActivity(intent)
        }

        // Semester Guide Button - ADD THIS
        findViewById<MaterialButton>(R.id.btnSemesterGuide).setOnClickListener {
            val intent = Intent(this, SemesterGuideActivity::class.java).apply {
                putExtra("branch", branch)
                putExtra("year", year)
                putExtra("goal", goal)
                putExtra("name", name)
            }
            startActivity(intent)
        }

        // AI Assistant Button
        findViewById<MaterialButton>(R.id.btnAssistant).setOnClickListener {
            val intent = Intent(this, ChatbotActivity::class.java).apply {
                putExtra("userName", name)
                putExtra("userBranch", branch)
                putExtra("userYear", year)
                putExtra("userGoal", goal)
            }
            startActivity(intent)
        }

        // Campus Resources Button
        findViewById<MaterialButton>(R.id.btnResources).setOnClickListener {
            val intent = Intent(this, CampusResourcesActivity::class.java).apply {
                putExtra("branch", branch)
                putExtra("year", year)
            }
            startActivity(intent)
        }

        // Opportunities Button
        findViewById<MaterialButton>(R.id.btnOpportunities).setOnClickListener {
            val intent = Intent(this, OpportunitiesActivity::class.java).apply {
                putExtra("goal", goal)
                putExtra("branch", branch)
            }
            startActivity(intent)
        }

        // Stats Cards Click Listeners
        findViewById<CardView>(R.id.cardProgress).setOnClickListener {
            showProgressDetails()
        }

        findViewById<CardView>(R.id.cardTarget).setOnClickListener {
            showTargetDetails()
        }
    }

    private fun showProgressDetails() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Your Progress Track")
            .setMessage(getProgressMessage())
            .setPositiveButton("Got it", null)
            .setNeutralButton("View Roadmap") { _, _ ->
                findViewById<MaterialButton>(R.id.btnRoadmap).performClick()
            }
            .show()
    }

    private fun getProgressMessage(): String {
        return when {
            year.contains("1st") -> """
                🎯 First Year Focus:
                
                ✅ Completed: Getting started
                📋 Next Steps:
                • Build strong fundamentals
                • Explore different domains
                • Join student clubs
                • Start learning basics
                
                💡 Tip: Focus on CGPA > 8.0
            """.trimIndent()

            year.contains("2nd") -> """
                🎯 Second Year Progress:
                
                ✅ Completed: Foundation ready
                📋 Next Steps:
                • Start core subjects
                • Learn DSA (if CSE)
                • Build first project
                • Apply for internships
                
                💡 Tip: Start LeetCode basics
            """.trimIndent()

            year.contains("3rd") -> """
                🎯 Third Year Progress:
                
                ✅ Completed: Core skills
                📋 Next Steps:
                • Advanced projects
                • Internship applications
                • Resume building
                • Networking
                
                💡 Tip: Critical year for placements!
            """.trimIndent()

            year.contains("4th") -> """
                🎯 Final Year Progress:
                
                ✅ Completed: Almost there!
                📋 Next Steps:
                • Placement preparation
                • GATE revision (if applicable)
                • Major project
                • Job applications
                
                💡 Tip: Give your best this year!
            """.trimIndent()

            else -> "Track your progress by completing tasks!"
        }
    }

    private fun showTargetDetails() {
        val targetMessage = when {
            goal.contains("Placement") -> """
                🎯 Your Placement Target:
                
                📅 Timeline: ${getPlacementTimeline()}
                
                🎯 Companies to Target:
                • ${getTopCompanies()}
                
                📊 Required Skills:
                • ${getRequiredSkills()}
                
                ✅ Action Items:
                • Update resume
                • Practice aptitude daily
                • Take mock interviews
                • Build portfolio projects
                
                🚀 Ready to crack placements?
            """.trimIndent()

            goal.contains("GATE") -> """
                📚 Your GATE Target:
                
                📅 Exam: February (next year)
                🎯 Target Rank: Top 500
                
                📊 Preparation Status:
                • Syllabus coverage: 40%
                • PYQs solved: 30%
                • Mock tests: 5 completed
                
                ✅ This Week's Goals:
                • Complete 2 subjects
                • Solve 50 PYQs
                • Take 1 mock test
                
                💪 Keep pushing!
            """.trimIndent()

            goal.contains("Higher Studies") -> """
                🎓 Higher Studies Target:
                
                🎯 Dream Universities:
                • MIT, Stanford (USA)
                • Oxford, Cambridge (UK)
                • NUS (Singapore)
                • IITs, IISc (India)
                
                📝 Requirements:
                • GRE: 320+
                • TOEFL: 100+
                • GPA: 8.5+
                • Research Papers: 2+
                
                📅 Timeline:
                • Start prep: Now
                • GRE by: 6 months
                • Applications: 8-10 months
                
                🌍 Your global journey starts here!
            """.trimIndent()

            else -> """
                🚀 Your Career Target:
                
                🎯 Short-term (6 months):
                • Build skills
                • Network actively
                • Create portfolio
                
                🎯 Long-term (2 years):
                • Career advancement
                • Industry recognition
                • Leadership roles
                
                Let's make it happen! 💪
            """.trimIndent()
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Your Goal: ${goal.take(30)}")
            .setMessage(targetMessage)
            .setPositiveButton("Let's do it!") { _, _ ->
                // Optional: Navigate to relevant section
            }
            .show()
    }

    private fun getPlacementTimeline(): String {
        return when {
            year.contains("1st") -> "3-4 years from now"
            year.contains("2nd") -> "2-3 years from now"
            year.contains("3rd") -> "6-12 months from now"
            year.contains("4th") -> "0-6 months from now"
            else -> "Plan ahead"
        }
    }

    private fun getTopCompanies(): String {
        return when (branch) {
            "CSE", "Computer Science" -> "Google, Microsoft, Amazon, Flipkart"
            "ECE", "Electronics" -> "Intel, Qualcomm, Texas Instruments"
            "EEE", "Electrical" -> "Siemens, GE, L&T, PSUs"
            else -> "Top MNCs and Core companies"
        }
    }

    private fun getRequiredSkills(): String {
        return when (branch) {
            "CSE", "Computer Science" -> "DSA, System Design, Web Dev, Aptitude"
            "ECE", "Electronics" -> "VLSI, Embedded Systems, MATLAB, Communication"
            "EEE", "Electrical" -> "Power Systems, Control Systems, PLC, Circuit Design"
            else -> "Technical + Soft Skills combination"
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh progress when returning to dashboard
        calculateProgress()
    }
}