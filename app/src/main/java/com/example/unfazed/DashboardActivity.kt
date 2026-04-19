package com.example.unfazed

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DashboardActivity : AppCompatActivity() {

    // Store user data
    private var name: String = ""
    private var branch: String = ""
    private var year: String = ""
    private var goal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 💾 LOAD DATA FROM DEVICE MEMORY INSTEAD OF INTENT
        val prefs = getSharedPreferences("UnfazedPrefs", MODE_PRIVATE)
        name = prefs.getString("name", "Student") ?: "Student"
        branch = prefs.getString("branch", "") ?: ""
        year = prefs.getString("year", "") ?: ""
        goal = prefs.getString("goal", "") ?: ""

        // 2. Setup UI and Listeners
        setupUserData()
        setupClickListeners()
    }

    private fun setupUserData() {
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val tvProfileSubtitle = findViewById<TextView>(R.id.tvProfileSubtitle)

        tvWelcome.text = "Hey $name! 👋"

        // Add a small hint that these are clickable now!
        tvProfileSubtitle.text = "$branch • $year\n(Tap for details)"

        // Hook up your awesome dialogs to the header text!
        tvWelcome.setOnClickListener { showProgressDetails() }
        tvProfileSubtitle.setOnClickListener { showTargetDetails() }
    }

    private fun setupClickListeners() {
        // Career Roadmap Card
        findViewById<MaterialCardView>(R.id.cardRoadmap).setOnClickListener {
            val intent = Intent(this, RoadmapActivity::class.java).apply {
                putExtra("name", name)
                putExtra("branch", branch)
                putExtra("year", year)
                putExtra("goal", goal)
            }
            startActivity(intent)
        }

        // AI Assistant Banner
        findViewById<MaterialCardView>(R.id.cardAIAssistant).setOnClickListener {
            val intent = Intent(this, ChatbotActivity::class.java).apply {
                putExtra("userName", name)
                putExtra("userBranch", branch)
                putExtra("userYear", year)
                putExtra("userGoal", goal)
            }
            startActivity(intent)
        }

        // Semester Guide Card
        findViewById<MaterialCardView>(R.id.cardSemester).setOnClickListener {
            val intent = Intent(this, SemesterGuideActivity::class.java).apply {
                putExtra("name", name)
                putExtra("branch", branch)
                putExtra("year", year)
                putExtra("goal", goal)
            }
            startActivity(intent)
        }

        // Campus Resources Card
        findViewById<MaterialCardView>(R.id.cardResources).setOnClickListener {
            val intent = Intent(this, CampusResourcesActivity::class.java).apply {
                putExtra("branch", branch)
                putExtra("year", year)
            }
            startActivity(intent)
        }

        // Opportunities Card
        findViewById<MaterialCardView>(R.id.cardOpportunities).setOnClickListener {
            val intent = Intent(this, OpportunitiesActivity::class.java).apply {
                putExtra("branch", branch)
                putExtra("goal", goal)
            }
            startActivity(intent)
        }
    }

    // ====================================================================
    // ALL YOUR CUSTOM PROGRESS & TARGET LOGIC IS SAFELY PRESERVED BELOW
    // ====================================================================

    private fun showProgressDetails() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Your Progress Track")
            .setMessage(getProgressMessage())
            .setPositiveButton("Got it", null)
            .setNeutralButton("View Roadmap") { _, _ ->
                findViewById<MaterialCardView>(R.id.cardRoadmap).performClick()
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
                • Learn DSA
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
            """.trimIndent()
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Your Goal: ${goal.take(30)}")
            .setMessage(targetMessage)
            .setPositiveButton("Let's do it!", null)
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
            "CSE", "Computer Science" -> "Google, Microsoft, Amazon, TCS Digital"
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
}