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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

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
        val allOpportunities = getAllOpportunities()
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

    private fun getAllOpportunities(): List<Opportunity> {
        return listOf(
            // Courses
            Opportunity(
                title = "Data Structures & Algorithms Masterclass",
                type = "Course",
                provider = "Coursera",
                deadline = "Start anytime",
                link = "https://coursera.org",
                matchScore = 95,
                description = "Complete DSA course with 200+ problems. Perfect for placement preparation.",
                duration = "3 months",
                level = "Beginner to Advanced",
                tags = listOf("DSA", "Placements", "Coding")
            ),
            Opportunity(
                title = "Machine Learning Specialization",
                type = "Course",
                provider = "Andrew Ng, DeepLearning.AI",
                deadline = "Rolling admission",
                link = "https://coursera.org",
                matchScore = 88,
                description = "Master ML algorithms and build real-world projects.",
                duration = "4 months",
                level = "Intermediate",
                tags = listOf("AI", "ML", "Python")
            ),
            Opportunity(
                title = "GATE CS Complete Preparation",
                type = "Course",
                provider = "Unacademy",
                deadline = "New batch: April 1",
                link = "https://unacademy.com",
                matchScore = if (goal.contains("GATE")) 98 else 75,
                description = "Full syllabus coverage with mock tests and PYQs.",
                duration = "6 months",
                level = "All Levels",
                tags = listOf("GATE", "CS", "Exam Prep")
            ),
            Opportunity(
                title = "Web Development Bootcamp",
                type = "Course",
                provider = "Udemy",
                deadline = "Sale ends soon",
                link = "https://udemy.com",
                matchScore = 85,
                description = "Full-stack development with MERN stack. Build 10+ projects.",
                duration = "2 months",
                level = "Beginner",
                tags = listOf("Web Dev", "React", "Node.js")
            ),

            // Internships
            Opportunity(
                title = "Software Engineering Intern",
                type = "Internship",
                provider = "Google",
                deadline = "Apply by May 15",
                link = "https://careers.google.com",
                matchScore = 92,
                description = "Summer internship 2024. Work on real products with Google engineers.",
                duration = "3 months",
                level = "2nd/3rd Year",
                tags = listOf("Paid", "Remote/Hybrid", "Top Company")
            ),
            Opportunity(
                title = "Microsoft Learn Student Ambassador",
                type = "Internship",
                provider = "Microsoft",
                deadline = "Applications open",
                link = "https://studentambassadors.microsoft.com",
                matchScore = 87,
                description = "Lead tech communities on campus, get Microsoft certification.",
                duration = "1 year",
                level = "All Years",
                tags = listOf("Leadership", "Community", "Microsoft")
            ),
            Opportunity(
                title = "Research Intern - AI/ML",
                type = "Internship",
                provider = "IIIT Hyderabad",
                deadline = "April 30",
                link = "https://iiit.ac.in",
                matchScore = 84,
                description = "Summer research internship in AI/ML with stipend.",
                duration = "2 months",
                level = "3rd Year+",
                tags = listOf("Research", "Stipend", "AI/ML")
            ),
            Opportunity(
                title = "Product Management Intern",
                type = "Internship",
                provider = "Amazon",
                deadline = "May 1",
                link = "https://amazon.jobs",
                matchScore = 82,
                description = "Learn product development, user research, and analytics.",
                duration = "2-3 months",
                level = "2nd/3rd Year",
                tags = listOf("Product", "Business", "Analytics")
            ),

            // Hackathons
            Opportunity(
                title = "Smart India Hackathon 2024",
                type = "Hackathon",
                provider = "Government of India",
                deadline = "Register by May 10",
                link = "https://sih.gov.in",
                matchScore = 96,
                description = "World's largest hackathon. Solve real government problems.",
                duration = "36 hours",
                level = "All Years",
                tags = listOf("National", "Cash Prizes", "Grand")
            ),
            Opportunity(
                title = "Google Solution Challenge",
                type = "Hackathon",
                provider = "Google Developers",
                deadline = "April 20",
                link = "https://developers.google.com",
                matchScore = 91,
                description = "Build solutions for UN Sustainable Development Goals.",
                duration = "2 months",
                level = "All Years",
                tags = listOf("Global", "Google", "Social Impact")
            ),
            Opportunity(
                title = "HackHarvard 2024",
                type = "Hackathon",
                provider = "Harvard University",
                deadline = "May 25",
                link = "https://hackharvard.io",
                matchScore = 86,
                description = "International hackathon. Travel scholarships available.",
                duration = "48 hours",
                level = "All Years",
                tags = listOf("International", "Travel", "Networking")
            ),
            Opportunity(
                title = "CodeChef Starters",
                type = "Hackathon",
                provider = "CodeChef",
                deadline = "Weekly",
                link = "https://codechef.com",
                matchScore = 78,
                description = "Weekly coding contest with prizes and ratings.",
                duration = "2 hours",
                level = "All Levels",
                tags = listOf("Coding", "Competitive", "Weekly")
            )
        )
    }

    // Data class
    data class Opportunity(
        val title: String,
        val type: String,
        val provider: String,
        val deadline: String,
        val link: String,
        val matchScore: Int,
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

                // Set type-specific styling
                when (opportunity.type) {
                    "Course" -> tvType.setBackgroundColor(itemView.context.getColor(R.color.primary))
                    "Internship" -> tvType.setBackgroundColor(itemView.context.getColor(R.color.secondary))
                    "Hackathon" -> tvType.setBackgroundColor(itemView.context.getColor(R.color.success))
                }

                btnApply.setOnClickListener {
                    showOpportunityDetails(opportunity)
                }

                cardView.setOnClickListener {
                    showOpportunityDetails(opportunity)
                }
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
                        
                        💡 Tip: Apply early for better chances!
                    """.trimIndent())
                    .setPositiveButton("Apply Now") { _, _ ->
                        // Open link in browser
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                        intent.data = android.net.Uri.parse(opportunity.link)
                        startActivity(intent)
                    }
                    .setNeutralButton("Save for Later") { _, _ ->
                        MaterialAlertDialogBuilder(itemView.context)
                            .setTitle("Saved!")
                            .setMessage("This opportunity has been saved to your profile.")
                            .setPositiveButton("OK", null)
                            .show()
                    }
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