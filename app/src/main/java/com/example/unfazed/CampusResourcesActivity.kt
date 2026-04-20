package com.example.unfazed

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CampusResourcesActivity : AppCompatActivity() {

    private lateinit var rvResources: RecyclerView
    private lateinit var chipGroup: ChipGroup
    private lateinit var tvNoResults: TextView

    private var currentFilter = "All"
    private var branch = ""
    private var year = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campus_resources)

        branch = intent.getStringExtra("branch") ?: ""
        year = intent.getStringExtra("year") ?: ""

        setupToolbar()
        initViews()
        setupFilters()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "AU Campus Resources"
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initViews() {
        rvResources = findViewById(R.id.rvResources)
        chipGroup = findViewById(R.id.chipGroup)
        tvNoResults = findViewById(R.id.tvNoResults)
    }

    private fun setupFilters() {
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.chipAll -> currentFilter = "All"
                R.id.chipLibraries -> currentFilter = "Library"
                R.id.chipFacilities -> currentFilter = "Facility"
                R.id.chipWellness -> currentFilter = "Wellness"
                R.id.chipSupport -> currentFilter = "Support"
                R.id.chipResearch -> currentFilter = "Research" // 🆕 New Filter
                else -> currentFilter = "All"
            }
            filterResources()
        }
    }

    private fun setupRecyclerView() {
        rvResources.layoutManager = LinearLayoutManager(this)
        filterResources()
    }

    private fun filterResources() {
        val allResources = getAllResources()
        val filtered = if (currentFilter == "All") {
            allResources
        } else {
            allResources.filter { it.type == currentFilter }
        }

        if (filtered.isEmpty()) {
            tvNoResults.visibility = View.VISIBLE
            rvResources.visibility = View.GONE
        } else {
            tvNoResults.visibility = View.GONE
            rvResources.visibility = View.VISIBLE
            rvResources.adapter = ResourceAdapter(filtered)
        }
    }

    // 🧠 THE EXPANDED ANDHRA UNIVERSITY DATABASE
    private fun getAllResources(): List<CampusResource> {
        return listOf(
            // 🔬 ADVANCED RESEARCH CENTRES (New!)
            CampusResource(
                name = "Centre for Cyber Security, AI-ML & Data Analytics",
                type = "Research",
                description = "Advanced multi-disciplinary research centre executing projects addressing contemporary tech challenges in AI, Machine Learning, and Cyber Security.",
                location = "AU Campus",
                accessInfo = "Open for research scholars and specialized B.Tech/M.Tech projects.",
                icon = "🛡️",
                features = listOf("AI/ML Models", "Cyber Security Research", "Data Analytics")
            ),
            CampusResource(
                name = "Centre for Nanotechnology & CoE",
                type = "Research",
                description = "State-of-the-art Centre of Excellence focused on Nanotechnology research and material sciences.",
                location = "AU Science Campus",
                accessInfo = "Prior permission required from Centre Director.",
                icon = "🧬",
                features = listOf("Nanomaterials", "Advanced Synthesis", "Centre of Excellence")
            ),
            CampusResource(
                name = "Centre for Intellectual Property Rights",
                type = "Research",
                description = "Provides crucial guidance for students and faculty on filing patents, copyrights, and protecting innovative ideas.",
                location = "AU Campus",
                accessInfo = "Walk-in for consultation on IP.",
                icon = "©️",
                features = listOf("Patent Filing", "Copyrights", "Innovation Protection")
            ),
            CampusResource(
                name = "AU Skill Development Centre",
                type = "Support",
                description = "Dedicated to upskilling students to make them industry-ready with contemporary tools and methodologies.",
                location = "AU Campus",
                accessInfo = "Check for upcoming batch enrollments.",
                icon = "📈",
                features = listOf("Industry Training", "Certifications", "Soft Skills")
            ),

            // 🚀 INCUBATION & INNOVATION
            CampusResource(
                name = "a-hub (AU Incubation Council)",
                type = "Facility",
                description = "Provides an environment to translate knowledge and innovation into successful entrepreneurs. Helps in contributing to local economy and employment generation.",
                location = "P8JC+WF7, AU North Campus",
                accessInfo = "Open to student innovators and startups.",
                icon = "🚀",
                features = listOf("Incubation Space", "Entrepreneurial Ecosystem", "Startup Support")
            ),
            CampusResource(
                name = "NASSCOM Centre of Excellence (IoT & AI)",
                type = "Facility",
                description = "Creates innovative applications across verticals (Smart City, Smart Health, Agriculture). Builds industry-capable talent and startup communities for IoT and AI.",
                location = "North Campus, opp. Dept of Marine Engineering",
                accessInfo = "Accessible for R&D, startups, and training.",
                icon = "🤖",
                features = listOf("IoT & AI Tech Stacks", "R&D Neutral Labs", "End-to-End Solutions")
            ),

            // 📚 LIBRARIES & LABS
            CampusResource(
                name = "Dr. V.S. Krishna Central Library",
                type = "Library",
                description = "One of the biggest libraries in the country with ~4,00,000 books. Subscribes to national and international journals.",
                location = "AU Central Campus",
                accessInfo = "Reading Room and Text Book section open 24/7",
                icon = "📚",
                features = listOf("24/7 Reading Room", "400k+ Books", "International Journals")
            ),
            CampusResource(
                name = "AUCEW Computer Labs & Campus WiFi",
                type = "Lab",
                description = "Four computer labs provided with 300 Mbps dedicated internet from A.U. Computer Centre. Entire college is connected via APSFL Intranet and WiFi.",
                location = "AUCEW Campus",
                accessInfo = "Student login credentials required for WiFi access.",
                icon = "🌐",
                features = listOf("300 Mbps High-Speed Line", "Campus-wide WiFi", "Intranet Access")
            ),
            CampusResource(
                name = "Advanced Analytical Laboratory (AAL)",
                type = "Lab",
                description = "Houses highly specialized scientific instruments including a Scanning Electron Microscope (SEM) and Micropulse Lidar.",
                location = "Various Science Depts",
                accessInfo = "Prior permission required from respective HODs.",
                icon = "🔬",
                features = listOf("Scanning Electron Microscope", "Atmospheric Aerosol Profiling", "Advanced Instrumentation")
            ),

            // 🛡️ STUDENT SUPPORT
            CampusResource(
                name = "Anti-Ragging & Grievance Redressal Cell",
                type = "Support",
                description = "Ensures a 100% ragging-free campus. Addresses grievances in association with the Women's Grievance Cell and Internal Complaints Committee (ICC).",
                location = "Principal's Office / Admin Block",
                accessInfo = "Strictly confidential. Available for all students.",
                icon = "🛡️",
                features = listOf("Zero Tolerance Policy", "Women's Grievance Cell", "Internal Complaints Committee")
            ),
            CampusResource(
                name = "Training & Placement Cell",
                type = "Support",
                description = "Dedicated cell for employment information, career guidance bureau, and campus placement drives.",
                location = "AU Campus",
                accessInfo = "Walk-in during working hours.",
                icon = "💼",
                features = listOf("Campus Placements", "Career Guidance", "Employment Info")
            ),

            // 🏥 WELLNESS & SPORTS
            CampusResource(
                name = "Sports Complex & Indoor Playgrounds",
                type = "Facility",
                description = "Includes open-air grounds (Silver & Golden Jubilee) for Tennis, Shuttle Badminton, and Throw Ball, plus indoor facilities for Chess, Carroms, and a Gymnasium.",
                location = "College Campus",
                accessInfo = "Open to all registered AU students.",
                icon = "🏸",
                features = listOf("Indoor Games", "Outdoor Courts", "Gymnasium")
            ),
            CampusResource(
                name = "Psychological Services",
                type = "Wellness",
                description = "Professional psychological assessment and counseling available for student well-being and stress management.",
                location = "Department of Psychology and Parapsychology",
                accessInfo = "Prior appointment required. Call: 2710031 or 2844430.",
                icon = "🧠",
                features = listOf("Mental Health Support", "Stress Counseling", "Assessments")
            )
        )
    }

    // Data class
    data class CampusResource(
        val name: String,
        val type: String,
        val description: String,
        val location: String,
        val accessInfo: String,
        val icon: String,
        val features: List<String> = emptyList()
    )

    // Adapter
    inner class ResourceAdapter(private val resources: List<CampusResource>) :
        RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_campus_resource, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(resources[position])
        }

        override fun getItemCount() = resources.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cardView: CardView = itemView.findViewById(R.id.cardResource)
            private val tvIcon: TextView = itemView.findViewById(R.id.tvIcon)
            private val tvName: TextView = itemView.findViewById(R.id.tvName)
            private val tvType: TextView = itemView.findViewById(R.id.tvType)
            private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
            private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
            private val tvAccess: TextView = itemView.findViewById(R.id.tvAccess)
            private val btnMoreInfo: TextView = itemView.findViewById(R.id.btnMoreInfo)

            fun bind(resource: CampusResource) {
                tvIcon.text = resource.icon
                tvName.text = resource.name
                tvType.text = resource.type
                tvDescription.text = resource.description
                tvLocation.text = "📍 ${resource.location}"
                tvAccess.text = "🔑 ${resource.accessInfo}"

                // 🎨 Modernized Color Coding based on resource type
                val tagColor = when (resource.type) {
                    "Library" -> "#6C63FF" // Purple
                    "Facility" -> "#F59E0B" // Amber
                    "Wellness" -> "#10B981" // Emerald Green
                    "Support" -> "#EC4899" // Pink
                    "Lab" -> "#06B6D4" // Cyan
                    "Research" -> "#6366F1" // 🆕 Deep Indigo for Research Centres
                    else -> "#64748B" // Slate Gray
                }
                tvType.setBackgroundColor(Color.parseColor(tagColor))

                btnMoreInfo.setOnClickListener { showResourceDetails(resource) }
                cardView.setOnClickListener { showResourceDetails(resource) }
            }

            private fun showResourceDetails(resource: CampusResource) {
                val featuresText = if (resource.features.isNotEmpty()) {
                    "\n\n✨ Key Highlights:\n" + resource.features.joinToString("\n") { "• $it" }
                } else ""

                MaterialAlertDialogBuilder(itemView.context)
                    .setTitle(resource.name)
                    .setMessage("""
                        ${resource.description}
                        
                        📍 Location: ${resource.location}
                        🔑 Access: ${resource.accessInfo}
                        $featuresText
                    """.trimIndent())
                    .setPositiveButton("Got it", null)
                    .show()
            }
        }
    }
}