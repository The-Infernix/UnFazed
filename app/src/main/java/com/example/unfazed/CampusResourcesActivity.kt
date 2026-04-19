package com.example.unfazed

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
import com.google.android.material.chip.Chip
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
        supportActionBar?.title = "Campus Resources"
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
                R.id.chipLabs -> currentFilter = "Lab"
                R.id.chipLibraries -> currentFilter = "Library"
                R.id.chipFacilities -> currentFilter = "Facility"
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

    private fun getAllResources(): List<CampusResource> {
        return listOf(
            // Libraries
            CampusResource(
                name = "Central Library",
                type = "Library",
                description = "Largest library on campus with 50,000+ books, journals, and digital resources. Silent study zones available.",
                location = "Block A, 2nd Floor",
                accessInfo = "Student ID required. Open Mon-Sat: 8 AM - 10 PM, Sun: 10 AM - 6 PM",
                icon = "📚",
                features = listOf("WiFi", "AC", "Digital Section", "Study Rooms")
            ),
            CampusResource(
                name = "Digital Library",
                type = "Library",
                description = "Access to online journals, e-books, and research papers. IEEE, Springer, ScienceDirect access.",
                location = "Block A, 3rd Floor",
                accessInfo = "24/7 access with student login credentials",
                icon = "💻",
                features = listOf("Online Access", "Printing", "Research Support")
            ),
            CampusResource(
                name = "Department Library",
                type = "Library",
                description = "Subject-specific books and reference materials for $branch students.",
                location = "$branch Department, 1st Floor",
                accessInfo = "Department hours: 9 AM - 5 PM",
                icon = "📖",
                features = listOf("Subject Experts", "Previous Year Papers", "Reference Books")
            ),

            // Labs
            CampusResource(
                name = "Computer Labs",
                type = "Lab",
                description = "Modern computer labs with high-end systems. All required software installed.",
                location = "Block B, Ground Floor",
                accessInfo = "Open lab: 9 AM - 8 PM. Book online for project work.",
                icon = "🖥️",
                features = listOf("100+ Systems", "Latest Software", "Printing", "24/7 Project Lab")
            ),
            CampusResource(
                name = "${branch} Core Lab",
                type = "Lab",
                description = getCoreLabDescription(),
                location = "$branch Department, Ground Floor",
                accessInfo = "Faculty permission required. Lab hours: 9 AM - 6 PM",
                icon = "🔬",
                features = getCoreLabFeatures()
            ),
            CampusResource(
                name = "Research Lab",
                type = "Lab",
                description = "Advanced research facility for final year projects and PhD work.",
                location = "Block D, 2nd Floor",
                accessInfo = "By appointment only. Contact research coordinator.",
                icon = "🧪",
                features = listOf("Research Equipment", "Mentorship", "Publication Support")
            ),
            CampusResource(
                name = "Project Lab",
                type = "Lab",
                description = "24/7 lab for project work and competitive coding practice.",
                location = "Block B, 1st Floor",
                accessInfo = "24/7 access with ID card. First-come basis.",
                icon = "💡",
                features = listOf("24/7 Access", "Projector", "Whiteboard", "Discussion Area")
            ),

            // Facilities
            CampusResource(
                name = "Career Development Center",
                type = "Facility",
                description = "Placement training, resume reviews, mock interviews, and career counseling.",
                location = "Admin Block, Room 101",
                accessInfo = "Walk-in: Mon-Fri 10 AM - 4 PM. Appointment preferred.",
                icon = "💼",
                features = listOf("Resume Review", "Mock Interviews", "Workshops", "Placement Updates")
            ),
            CampusResource(
                name = "Entrepreneurship Cell",
                type = "Facility",
                description = "Support for student startups, incubation, and funding guidance.",
                location = "Student Activity Center",
                accessInfo = "Join as member. Monthly meetups every 1st Saturday.",
                icon = "🚀",
                features = listOf("Mentorship", "Networking", "Funding Support", "Workshops")
            ),
            CampusResource(
                name = "Research Cell",
                type = "Facility",
                description = "Guidance for research papers, patents, and conference submissions.",
                location = "Block D, 3rd Floor",
                accessInfo = "Faculty mentorship available. Submit research proposal.",
                icon = "📝",
                features = listOf("Paper Writing", "Conference Support", "Patent Filing", "Collaboration")
            ),
            CampusResource(
                name = "Student Activity Center",
                type = "Facility",
                description = "Hub for student clubs, events, and extracurricular activities.",
                location = "Near Canteen",
                accessInfo = "Open 8 AM - 8 PM. Book rooms through student portal.",
                icon = "🎯",
                features = listOf("Club Rooms", "Event Space", "Cafeteria", "Recreation")
            ),
            CampusResource(
                name = "Makerspace",
                type = "Facility",
                description = "Workshop with 3D printers, CNC machines, and prototyping tools.",
                location = "Block C, Basement",
                accessInfo = "Training required first. Then 24/7 access for members.",
                icon = "⚙️",
                features = listOf("3D Printers", "CNC", "Electronics", "Woodworking")
            )
        )
    }

    private fun getCoreLabDescription(): String {
        return when {
            branch.contains("CSE") -> "Programming lab with AI/ML setup, cloud access, and development tools."
            branch.contains("ECE") -> "Electronics lab with oscilloscopes, signal generators, and FPGA kits."
            branch.contains("EEE") -> "Electrical machines lab with motors, generators, and power systems."
            else -> "Department lab with specialized equipment for $branch students."
        }
    }

    private fun getCoreLabFeatures(): List<String> {
        return when {
            branch.contains("CSE") -> listOf("High-End PCs", "GPU Servers", "Cloud Access", "Latest IDEs")
            branch.contains("ECE") -> listOf("Oscilloscopes", "FPGA Kits", "Microcontrollers", "PCB Design")
            branch.contains("EEE") -> listOf("Motor Control", "Power Systems", "PLC Training", "Simulation Software")
            else -> listOf("Specialized Equipment", "Expert Guidance", "Project Support")
        }
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

                // Set type-specific styling
                when (resource.type) {
                    "Library" -> tvType.setBackgroundColor(itemView.context.getColor(R.color.primary))
                    "Lab" -> tvType.setBackgroundColor(itemView.context.getColor(R.color.secondary))
                    "Facility" -> tvType.setBackgroundColor(itemView.context.getColor(R.color.success))
                }

                btnMoreInfo.setOnClickListener {
                    showResourceDetails(resource)
                }

                cardView.setOnClickListener {
                    showResourceDetails(resource)
                }
            }

            private fun showResourceDetails(resource: CampusResource) {
                val featuresText = if (resource.features.isNotEmpty()) {
                    "\n\n✨ Features:\n" + resource.features.joinToString("\n") { "• $it" }
                } else ""

                MaterialAlertDialogBuilder(itemView.context)
                    .setTitle(resource.name)
                    .setMessage("""
                        ${resource.description}
                        
                        📍 Location: ${resource.location}
                        🔑 Access: ${resource.accessInfo}
                        $featuresText
                        
                        💡 Tip: Visit during off-peak hours for better experience!
                    """.trimIndent())
                    .setPositiveButton("Got it") { _, _ -> }
                    .setNeutralButton("Get Directions") { _, _ ->
                        // Show location in maps or provide directions
                        MaterialAlertDialogBuilder(itemView.context)
                            .setTitle("Location: ${resource.location}")
                            .setMessage("Ask any student or staff for directions to ${resource.location}. It's a well-known location on campus.")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                    .show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}