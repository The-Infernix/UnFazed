package com.example.unfazed

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class RoadmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roadmap)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Career Roadmap"

        val branch = intent.getStringExtra("branch") ?: ""
        val goal = intent.getStringExtra("goal") ?: ""
        val year = intent.getStringExtra("year") ?: ""

        val tvGoalTitle = findViewById<TextView>(R.id.tvGoalTitle)
        val tvRoadmap = findViewById<TextView>(R.id.tvRoadmap)

        tvGoalTitle.text = goal

        val roadmap = generateRoadmap(branch, goal, year)
        tvRoadmap.text = roadmap
    }

    private fun generateRoadmap(branch: String, goal: String, year: String): String {
        return when {
            goal.contains("Placement") -> getPlacementRoadmap(branch)
            goal.contains("GATE") -> getGATERoadmap(branch)
            goal.contains("Higher Studies") -> getHigherStudiesRoadmap(branch)
            goal.contains("Entrepreneurship") -> getEntrepreneurshipRoadmap()
            else -> getGeneralRoadmap()
        }
    }

    private fun getPlacementRoadmap(branch: String): String {
        return buildString {
            appendLine("🎯 YOUR PLACEMENT ROADMAP\n")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━\n")

            if (branch.contains("CSE") || branch.contains("Computer")) {
                appendLine("📌 YEAR 1-2: Foundation")
                appendLine("   • Learn Python/Java fundamentals")
                appendLine("   • Master Data Structures & Algorithms")
                appendLine("   • Solve problems on LeetCode (100+ problems)")
                appendLine("")
                appendLine("📌 YEAR 2-3: Skill Building")
                appendLine("   • Build 2-3 projects (Web/Mobile)")
                appendLine("   • Learn Git & GitHub")
                appendLine("   • Start Competitive Programming")
                appendLine("")
                appendLine("📌 YEAR 3-4: Placement Prep")
                appendLine("   • Practice Aptitude & Reasoning")
                appendLine("   • Prepare for HR interviews")
                appendLine("   • Apply for internships")
                appendLine("   • Mock interviews with seniors")
                appendLine("")
                appendLine("💡 Top Companies to Target:")
                appendLine("   • Google • Microsoft • Amazon • Flipkart")
            } else {
                appendLine("📌 Core Technical Skills:")
                appendLine("   • Master your core subjects")
                appendLine("   • Get certified in industry tools")
                appendLine("   • Work on live projects")
                appendLine("")
                appendLine("📌 Soft Skills:")
                appendLine("   • Communication & teamwork")
                appendLine("   • Leadership & problem solving")
                appendLine("")
                appendLine("📌 Placement Strategy:")
                appendLine("   • Apply to PSUs & Core companies")
                appendLine("   • Prepare for technical interviews")
                appendLine("   • Build a strong portfolio")
            }

            appendLine("\n━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("⭐ Pro Tip: Start applying from 3rd year!")
        }
    }

    private fun getGATERoadmap(branch: String): String {
        return buildString {
            appendLine("📚 GATE EXAM PREPARATION ROADMAP\n")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━\n")
            appendLine("📌 Year 1-2:")
            appendLine("   • Strengthen core subject fundamentals")
            appendLine("   • Start with Engineering Mathematics")
            appendLine("   • Build problem-solving speed")
            appendLine("")
            appendLine("📌 Year 3:")
            appendLine("   • Complete syllabus by December")
            appendLine("   • Solve previous year papers (last 10 years)")
            appendLine("   • Take weekly mock tests")
            appendLine("")
            appendLine("📌 Year 4:")
            appendLine("   • Revision & formula memorization")
            appendLine("   • Attempt full-length mock tests")
            appendLine("   • Analyze weak areas & improve")
            appendLine("")
            appendLine("📚 Recommended Resources:")
            appendLine("   • Made Easy Publications")
            appendLine("   • Ace Academy Notes")
            appendLine("   • NPTEL Video Lectures")
            appendLine("\n━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("🎯 Target: Top 100 rank for IITs/NITs")
        }
    }

    private fun getHigherStudiesRoadmap(branch: String): String {
        return buildString {
            appendLine("🎓 MS/PhD ABROAD ROADMAP\n")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━\n")
            appendLine("📌 Year 1-2:")
            appendLine("   • Maintain high GPA (8.5+)")
            appendLine("   • Start research projects")
            appendLine("   • Build relationship with professors")
            appendLine("")
            appendLine("📌 Year 3:")
            appendLine("   • Prepare for GRE/TOEFL/IELTS")
            appendLine("   • Work on research papers")
            appendLine("   • Apply for summer internships")
            appendLine("")
            appendLine("📌 Year 4:")
            appendLine("   • Shortlist universities")
            appendLine("   • Get strong LORs")
            appendLine("   • Write compelling SOP")
            appendLine("   • Apply for scholarships")
            appendLine("")
            appendLine("🌍 Top Destinations:")
            appendLine("   • USA • Germany • Canada • Australia")
        }
    }

    private fun getEntrepreneurshipRoadmap(): String {
        return buildString {
            appendLine("💼 ENTREPRENEURSHIP ROADMAP\n")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━\n")
            appendLine("📌 Year 1-2:")
            appendLine("   • Identify problem areas")
            appendLine("   • Learn business fundamentals")
            appendLine("   • Network with entrepreneurs")
            appendLine("")
            appendLine("📌 Year 3:")
            appendLine("   • Build MVP of your idea")
            appendLine("   • Join startup competitions")
            appendLine("   • Apply for incubation centers")
            appendLine("")
            appendLine("📌 Year 4:")
            appendLine("   • Launch your startup")
            appendLine("   • Seek funding/grants")
            appendLine("   • Scale your venture")
            appendLine("")
            appendLine("🏆 Startup Resources:")
            appendLine("   • Startup India Initiative")
            appendLine("   • T-Hub, Y Combinator")
        }
    }

    private fun getGeneralRoadmap(): String {
        return """
            🎯 YOUR CUSTOMIZED ROADMAP
            
            We're building a personalized roadmap for you!
            
            📌 Focus on:
            • Academic excellence
            • Skill development
            • Industry networking
            • Portfolio building
            
            🔜 More personalized recommendations coming soon!
        """.trimIndent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}