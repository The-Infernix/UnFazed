package com.example.unfazed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OpportunitiesActivity : AppCompatActivity() {

    private lateinit var rvOpportunities: RecyclerView
    private lateinit var chipGroup: ChipGroup
    private lateinit var tvNoResults: TextView

    private var currentFilter = "All"
    private var goal = ""
    private var branch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opportunities)

        goal = intent.getStringExtra("goal") ?: ""
        branch = intent.getStringExtra("branch") ?: ""

        setupToolbar()
        initViews()
        setupFilters()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Recommended Opportunities"
    }

    private fun initViews() {
        rvOpportunities = findViewById(R.id.rvOpportunities)
        chipGroup = findViewById(R.id.chipGroup)
        tvNoResults = findViewById(R.id.tvNoResults)
    }

    private fun setupFilters() {
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.chipAll -> currentFilter = "All"
                R.id.chipCourses -> currentFilter = "Course"
                R.id.chipInternships -> currentFilter = "Internship"
                R.id.chipHackathons -> currentFilter = "Hackathon"
            }
            filterOpportunities()
        }
    }

    private fun setupRecyclerView() {
        rvOpportunities.layoutManager = LinearLayoutManager(this)
        filterOpportunities()
    }

    private fun filterOpportunities() {
        // Fetch data and dynamically sort by the highest match score
        val allOpportunities = getDynamicOpportunities(branch, goal)
            .sortedByDescending { it.matchScore }

        val filtered = if (currentFilter == "All") {
            allOpportunities
        } else {
            allOpportunities.filter { it.type == currentFilter }
        }

        if (filtered.isEmpty()) {
            tvNoResults.visibility = View.VISIBLE
            rvOpportunities.visibility = View.GONE
        } else {
            tvNoResults.visibility = View.GONE
            rvOpportunities.visibility = View.VISIBLE
            rvOpportunities.adapter = OpportunityAdapter(filtered)
        }
    }

    // 🧠 Dynamic Database and Match Scoring
    private fun getDynamicOpportunities(userBranch: String, userGoal: String): List<Opportunity> {
        val opportunities = mutableListOf<Opportunity>()

        // Helper function to calculate a smart score
        fun calculateScore(targetBranches: List<String>, targetGoals: List<String>, baseScore: Int): Int {
            var score = baseScore
            val isCSE = userBranch.contains("CSE") || userBranch.contains("Computer")

            if (targetBranches.contains("ALL") || (isCSE && targetBranches.contains("CSE")) || targetBranches.any { userBranch.contains(it) }) {
                score += 15
            }
            if (targetGoals.contains("ALL") || targetGoals.any { userGoal.contains(it) }) {
                score += 15
            }
            return score.coerceAtMost(99) // Cap at 99%
        }

        // ==========================================
        // 🏆 TIER 1: HIGH-STAKES COMPETITIONS (Direct Jobs)
        // ==========================================
        opportunities.add(
            Opportunity(
                title = "TCS CodeVita (Season 12)",
                type = "Hackathon",
                provider = "Tata Consultancy Services",
                deadline = "Registrations open Oct",
                link = "https://codevita.tcsapps.com/",
                matchScore = calculateScore(listOf("CSE", "ECE", "EEE"), listOf("Placement"), 75),
                description = "Guaranteed interview call for TCS Ninja, Digital, or Prime roles if you clear round 1. Global ranking.",
                duration = "6 hours",
                level = "All Years",
                tags = listOf("Direct Hiring", "Coding", "High Package")
            )
        )

        opportunities.add(
            Opportunity(
                title = "Flipkart GRiD 6.0",
                type = "Hackathon",
                provider = "Flipkart",
                deadline = "Registrations open June",
                link = "https://unstop.com/flipkart-grid",
                matchScore = calculateScore(listOf("CSE", "ECE", "Mechanical"), listOf("Placement"), 70),
                description = "Tracks in Software Development, Information Security, and Robotics. Top teams get PPOs and massive cash prizes.",
                duration = "2 Months",
                level = "Pre-final/Final Year",
                tags = listOf("PPO", "E-commerce", "Robotics")
            )
        )

        opportunities.add(
            Opportunity(
                title = "e-Yantra Robotics Competition",
                type = "Hackathon",
                provider = "IIT Bombay & MoE",
                deadline = "August",
                link = "https://e-yantra.org/",
                matchScore = calculateScore(listOf("Mechanical", "ECE", "EEE", "CSE"), listOf("Placement", "Higher Studies"), 70),
                description = "India's premier robotics competition. You are shipped real hardware kits to solve complex automation tasks.",
                duration = "6 Months",
                level = "All Years",
                tags = listOf("Hardware", "Core Engineering", "IIT Bombay")
            )
        )

        // ==========================================
        // 🌍 TIER 2: ELITE INTERNSHIPS & FELLOWSHIPS
        // ==========================================
        opportunities.add(
            Opportunity(
                title = "Mitacs Globalink Research Internship",
                type = "Internship",
                provider = "Govt. of Canada",
                deadline = "Apply by September",
                link = "https://www.mitacs.ca/",
                matchScore = calculateScore(listOf("ALL"), listOf("Higher Studies"), 75),
                description = "Fully funded 12-week research internship at top Canadian universities. The holy grail for MS/PhD aspirants.",
                duration = "12 weeks",
                level = "Pre-final Year (3rd Year)",
                tags = listOf("Fully Funded", "Research", "Canada", "MS/PhD")
            )
        )

        opportunities.add(
            Opportunity(
                title = "MLH Fellowship",
                type = "Internship",
                provider = "Major League Hacking (Meta/GitHub)",
                deadline = "Rolling Admissions",
                link = "https://fellowship.mlh.io/",
                matchScore = calculateScore(listOf("CSE"), listOf("Placement", "Higher Studies"), 70),
                description = "Remote stipend-based fellowship. Contribute to major open-source projects like React, Jest, or AWS.",
                duration = "12 weeks",
                level = "Intermediate to Advanced",
                tags = listOf("Remote", "Stipend", "Open Source")
            )
        )

        opportunities.add(
            Opportunity(
                title = "AICTE EduSkills Virtual Internship",
                type = "Internship",
                provider = "AICTE (AWS, Palo Alto)",
                deadline = "Cohort starting soon",
                link = "https://internship.aicte-india.org/",
                matchScore = calculateScore(listOf("CSE", "ECE", "IT"), listOf("Placement"), 65),
                description = "Learn Cloud Computing, Cybersecurity, or RPA. Get an official Govt of India and corporate joint certificate.",
                duration = "8 weeks",
                level = "2nd/3rd Year",
                tags = listOf("Govt Certified", "Cloud", "Cybersecurity")
            )
        )

        // ==========================================
        // 💼 TIER 3: FOUNDER & STARTUP PROGRAMS
        // ==========================================
        opportunities.add(
            Opportunity(
                title = "Y Combinator Startup School",
                type = "Course",
                provider = "Y Combinator",
                deadline = "Self-paced",
                link = "https://www.startupschool.org/",
                matchScore = calculateScore(listOf("ALL"), listOf("Entrepreneurship", "Startup"), 80),
                description = "Learn how to build a billion-dollar startup from the founders of Airbnb, Stripe, and Reddit. Free and online.",
                duration = "8 weeks",
                level = "Beginner to Advanced",
                tags = listOf("Founders", "Fundraising", "Silicon Valley")
            )
        )

        opportunities.add(
            Opportunity(
                title = "Tata Imagination Challenge",
                type = "Hackathon",
                provider = "Tata Group",
                deadline = "September",
                link = "https://www.tatacrucible.com/",
                matchScore = calculateScore(listOf("ALL"), listOf("Entrepreneurship", "Placement"), 65),
                description = "Pitch your innovative business idea. Winners get cash prizes and accelerated interviews with TAS (Tata Administrative Services).",
                duration = "1 Month",
                level = "All Years",
                tags = listOf("Business", "Innovation", "Tata")
            )
        )

        // ==========================================
        // 📚 TIER 4: ESSENTIAL COURSES & PREP
        // ==========================================
        opportunities.add(
            Opportunity(
                title = "AWS Academy Cloud Foundations",
                type = "Course",
                provider = "Amazon Web Services",
                deadline = "Self-paced",
                link = "https://aws.amazon.com/training/awsacademy/",
                matchScore = calculateScore(listOf("CSE", "ECE"), listOf("Placement"), 60),
                description = "Cloud skills are mandatory for software roles today. Get hands-on with EC2, S3, and Lambda to prepare for AWS certification.",
                duration = "20 hours",
                level = "Beginner",
                tags = listOf("Cloud", "AWS", "Certification")
            )
        )

        opportunities.add(
            Opportunity(
                title = "NPTEL / Swayam Local Chapter",
                type = "Course",
                provider = "IITs & IISc",
                deadline = "Enrollment closes Jan/July",
                link = "https://swayam.gov.in/",
                matchScore = calculateScore(listOf("Mechanical", "Civil", "EEE", "ECE"), listOf("GATE", "Higher Studies"), 75),
                description = "Transferable college credits and the absolute best resource for GATE core subject preparation.",
                duration = "8-12 weeks",
                level = "All Years",
                tags = listOf("GATE Prep", "IIT Faculty", "Credit Transfer")
            )
        )

        return opportunities
    }

    // Data class
    data class Opportunity(
        val title: String,
        val type: String,
        val provider: String,
        val deadline: String,
        val link: String,
        var matchScore: Int,
        val description: String = "",
        val duration: String = "",
        val level: String = "",
        val tags: List<String> = emptyList()
    )

    // Adapter
    inner class OpportunityAdapter(private val opportunities: List<Opportunity>) :
        RecyclerView.Adapter<OpportunityAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_opportunity, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(opportunities[position])
        }

        override fun getItemCount() = opportunities.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cardView: CardView = itemView.findViewById(R.id.cardOpportunity)
            private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            private val tvType: TextView = itemView.findViewById(R.id.tvType)
            private val tvProvider: TextView = itemView.findViewById(R.id.tvProvider)
            private val tvDeadline: TextView = itemView.findViewById(R.id.tvDeadline)
            private val tvMatchScore: TextView = itemView.findViewById(R.id.tvMatchScore)
            private val progressMatch: ProgressBar = itemView.findViewById(R.id.progressMatch)
            private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
            private val btnApply: TextView = itemView.findViewById(R.id.btnApply)

            fun bind(opportunity: Opportunity) {
                tvTitle.text = opportunity.title
                tvType.text = opportunity.type
                tvProvider.text = opportunity.provider
                tvDeadline.text = "📅 ${opportunity.deadline}"
                tvMatchScore.text = "${opportunity.matchScore}% Match"
                progressMatch.progress = opportunity.matchScore

                if (opportunity.description.isNotEmpty()) {
                    tvDescription.text = opportunity.description
                    tvDescription.visibility = View.VISIBLE
                } else {
                    tvDescription.visibility = View.GONE
                }

                btnApply.setOnClickListener { showOpportunityDetails(opportunity) }
                cardView.setOnClickListener { showOpportunityDetails(opportunity) }
            }

            private fun showOpportunityDetails(opportunity: Opportunity) {
                val tagsText = if (opportunity.tags.isNotEmpty()) {
                    "\n\n🏷️ Tags:\n" + opportunity.tags.joinToString(" • ")
                } else ""

                val durationText = if (opportunity.duration.isNotEmpty()) {
                    "\n⏱️ Duration: ${opportunity.duration}"
                } else ""

                val levelText = if (opportunity.level.isNotEmpty()) {
                    "\n📊 Level: ${opportunity.level}"
                } else ""

                MaterialAlertDialogBuilder(itemView.context)
                    .setTitle(opportunity.title)
                    .setMessage("""
                        ${opportunity.description}
                        
                        🏢 Provider: ${opportunity.provider}
                        📅 Deadline: ${opportunity.deadline}
                        $durationText$levelText
                        $tagsText
                        
                        🎯 Match Score: ${opportunity.matchScore}%
                    """.trimIndent())
                    .setPositiveButton("Apply Now") { _, _ ->
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                        intent.data = android.net.Uri.parse(opportunity.link)
                        itemView.context.startActivity(intent)
                    }
                    .setNeutralButton("Save for Later", null)
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}