package com.example.unfazed

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider

class SemesterGuideActivity : AppCompatActivity() {

    private lateinit var chipGroupSemester: ChipGroup
    private lateinit var sliderDaysLeft: Slider
    private lateinit var sliderStudyHours: Slider
    private lateinit var btnGenerate: MaterialButton
    private lateinit var layoutTimetable: LinearLayout
    private lateinit var tvDaysLeft: TextView
    private lateinit var tvStudyHours: TextView
    private lateinit var tvStrategy: TextView
    private lateinit var tvSubjects: TextView
    private lateinit var tvTimetable: TextView
    private lateinit var tvWeeklyPlan: TextView
    private lateinit var tvTips: TextView

    private var branch = ""
    private var year = ""
    private var selectedSemester = 1
    private var daysLeft = 60
    private var studyHoursPerDay = 4.0f  // Changed to Float

    // Subject data for each branch and semester
    private val subjectData = mapOf(
        "CSE" to mapOf(
            1 to listOf("Programming Fundamentals", "Engineering Mathematics-I", "Physics", "Basic Electrical Engineering", "English", "Environmental Science"),
            2 to listOf("Data Structures", "Engineering Mathematics-II", "Chemistry", "Object Oriented Programming", "Digital Logic Design", "Communication Skills"),
            3 to listOf("Discrete Mathematics", "Database Management Systems", "Computer Organization", "Operating Systems", "Python Programming", "Probability & Statistics"),
            4 to listOf("Design & Analysis of Algorithms", "Software Engineering", "Computer Networks", "Microprocessors", "Theory of Computation", "Management Skills"),
            5 to listOf("Artificial Intelligence", "Web Technologies", "Compiler Design", "Information Security", "Cloud Computing", "Elective-I"),
            6 to listOf("Machine Learning", "Big Data Analytics", "Mobile App Development", "Computer Graphics", "Elective-II", "Open Elective"),
            7 to listOf("Deep Learning", "Internet of Things", "Blockchain Technology", "Project Work-I", "Elective-III", "Industrial Training"),
            8 to listOf("Major Project", "Internship", "Technical Seminar", "Comprehensive Viva", "Elective-IV", "Entrepreneurship")
        ),
        "ECE" to mapOf(
            1 to listOf("Engineering Mathematics-I", "Physics", "Basic Electronics", "Network Theory", "English", "Programming in C"),
            2 to listOf("Engineering Mathematics-II", "Chemistry", "Electronic Devices", "Signals & Systems", "Digital Electronics", "Python Programming"),
            3 to listOf("Analog Circuits", "Pulse & Digital Circuits", "Electromagnetic Fields", "Control Systems", "Measurements & Instrumentation", "Probability & Statistics"),
            4 to listOf("Linear IC Applications", "Digital Signal Processing", "Microprocessors", "Communication Systems", "VLSI Design", "Management Skills"),
            5 to listOf("Embedded Systems", "Antenna & Wave Propagation", "Optical Communications", "Wireless Communications", "Elective-I", "Open Elective"),
            6 to listOf("Microwave Engineering", "Satellite Communication", "CMOS Design", "Image Processing", "Elective-II", "Industrial Training"),
            7 to listOf("IoT & Applications", "Robotics", "Project Work-I", "Elective-III", "Elective-IV", "Technical Seminar"),
            8 to listOf("Major Project", "Internship", "Comprehensive Viva", "Advanced Communication Systems", "Elective-V", "Entrepreneurship")
        ),
        "EEE" to mapOf(
            1 to listOf("Engineering Mathematics-I", "Physics", "Basic Electrical Engineering", "Network Theory", "English", "Programming in C"),
            2 to listOf("Engineering Mathematics-II", "Chemistry", "Electromagnetic Fields", "Electrical Machines-I", "Digital Electronics", "Python Programming"),
            3 to listOf("Electrical Machines-II", "Power Systems-I", "Control Systems", "Measurements", "Analog Electronics", "Probability & Statistics"),
            4 to listOf("Power Electronics", "Power Systems-II", "Microcontrollers", "Signal Processing", "Management Skills", "Open Elective"),
            5 to listOf("High Voltage Engineering", "Switchgear & Protection", "Renewable Energy", "Electrical Drives", "Elective-I", "Industrial Training"),
            6 to listOf("Smart Grid", "HVDC Transmission", "Power Quality", "Energy Management", "Elective-II", "Open Elective"),
            7 to listOf("Project Work-I", "Elective-III", "Elective-IV", "Technical Seminar", "Industrial Safety", "Entrepreneurship"),
            8 to listOf("Major Project", "Internship", "Comprehensive Viva", "Power System Operation", "Elective-V", "Management")
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semester_guide)

        branch = intent.getStringExtra("branch") ?: "CSE"
        year = intent.getStringExtra("year") ?: ""

        setupToolbar()
        initViews()
        setupListeners()
        preSelectSemester()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Smart Semester Planner"
    }

    private fun initViews() {
        chipGroupSemester = findViewById(R.id.chipGroupSemester)
        sliderDaysLeft = findViewById(R.id.sliderDaysLeft)
        sliderStudyHours = findViewById(R.id.sliderStudyHours)
        btnGenerate = findViewById(R.id.btnGenerate)
        layoutTimetable = findViewById(R.id.layoutTimetable)
        tvDaysLeft = findViewById(R.id.tvDaysLeft)
        tvStudyHours = findViewById(R.id.tvStudyHours)
        tvStrategy = findViewById(R.id.tvStrategy)
        tvSubjects = findViewById(R.id.tvSubjects)
        tvTimetable = findViewById(R.id.tvTimetable)
        tvWeeklyPlan = findViewById(R.id.tvWeeklyPlan)
        tvTips = findViewById(R.id.tvTips)
    }

    private fun setupListeners() {
        // Semester selection
        chipGroupSemester.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = findViewById<Chip>(checkedIds[0])
                selectedSemester = when (chip.text.toString()) {
                    "Semester 1" -> 1
                    "Semester 2" -> 2
                    "Semester 3" -> 3
                    "Semester 4" -> 4
                    "Semester 5" -> 5
                    "Semester 6" -> 6
                    "Semester 7" -> 7
                    "Semester 8" -> 8
                    else -> 1
                }
            }
        }

        // Days left slider
        sliderDaysLeft.addOnChangeListener { _, value, _ ->
            daysLeft = value.toInt()
            tvDaysLeft.text = "$daysLeft days left for exam"
        }

        // Study hours slider - FIXED: value is Float, studyHoursPerDay is Float
        sliderStudyHours.addOnChangeListener { _, value, _ ->
            studyHoursPerDay = value  // Now works - both are Float
            tvStudyHours.text = String.format("%.1f hours/day", studyHoursPerDay)
        }

        // Generate button
        btnGenerate.setOnClickListener {
            generateSmartTimetable()
            layoutTimetable.visibility = LinearLayout.VISIBLE
        }
    }

    private fun preSelectSemester() {
        // Auto-select semester based on year
        val semester = when {
            year.contains("1st") -> 1
            year.contains("2nd") -> 3
            year.contains("3rd") -> 5
            year.contains("4th") -> 7
            else -> 1
        }

        val chipId = when (semester) {
            1 -> R.id.chipSem1
            2 -> R.id.chipSem2
            3 -> R.id.chipSem3
            4 -> R.id.chipSem4
            5 -> R.id.chipSem5
            6 -> R.id.chipSem6
            7 -> R.id.chipSem7
            8 -> R.id.chipSem8
            else -> R.id.chipSem1
        }

        findViewById<Chip>(chipId).isChecked = true
        selectedSemester = semester
    }

    private fun generateSmartTimetable() {
        val subjects = getSubjectsForSemester()
        val strategy = generateExamStrategy()
        val timetable = generateDailyTimetable(subjects)
        val weeklyPlan = generateWeeklyPlan(subjects)
        val tips = generateStudyTips()

        tvStrategy.text = strategy
        tvSubjects.text = subjects.joinToString("\n") { "• $it" }
        tvTimetable.text = timetable
        tvWeeklyPlan.text = weeklyPlan
        tvTips.text = tips
    }

    private fun getSubjectsForSemester(): List<String> {
        val branchSubjects = subjectData[branch] ?: subjectData["CSE"]!!
        return branchSubjects[selectedSemester] ?: branchSubjects[1]!!
    }

    private fun generateExamStrategy(): String {
        val weeksLeft = daysLeft / 7
        val hoursPerWeek = studyHoursPerDay * 7
        val totalHours = daysLeft * studyHoursPerDay

        return buildString {
            appendLine("🎯 Based on your schedule:")
            appendLine("• Days left: $daysLeft days")
            appendLine("• Study hours/day: ${String.format("%.1f", studyHoursPerDay)} hours")
            appendLine("• Total study time: ${String.format("%.0f", totalHours)} hours")
            appendLine()

            when {
                daysLeft > 90 -> {
                    appendLine("📌 Phase 1 (Days 1-${daysLeft - 60}): Concept Building")
                    appendLine("   • Focus on understanding core concepts")
                    appendLine("   • Take detailed notes")
                    appendLine("   • Watch video lectures")
                    appendLine()
                    appendLine("📌 Phase 2 (Last 60 days): Intensive Practice")
                    appendLine("   • Solve problems daily")
                    appendLine("   • Take weekly tests")
                    appendLine("   • Revise weak topics")
                }
                daysLeft in 61..90 -> {
                    appendLine("📌 Phase 1 (Days 1-${daysLeft - 30}): Complete Syllabus")
                    appendLine("   • Cover remaining topics")
                    appendLine("   • Practice concept-wise problems")
                    appendLine()
                    appendLine("📌 Phase 2 (Last 30 days): Revision & Testing")
                    appendLine("   • Solve previous year papers")
                    appendLine("   • Take mock tests")
                    appendLine("   • Focus on weak areas")
                }
                daysLeft in 31..60 -> {
                    appendLine("⚠️ Time is limited! Focus on:")
                    appendLine("• High-weightage topics first")
                    appendLine("• Solve problems daily (2-3 hours)")
                    appendLine("• Take mock tests every 3 days")
                    appendLine("• Revise formulas/concepts daily")
                }
                daysLeft <= 30 -> {
                    appendLine("🚨 CRITICAL - Last minute preparation:")
                    appendLine("• Only revise what you already know")
                    appendLine("• Solve 1 full test daily")
                    appendLine("• Don't learn new topics")
                    appendLine("• Focus on time management")
                    appendLine("• Take care of health!")
                }
            }

            appendLine()
            appendLine("📊 Recommended weekly schedule:")
            appendLine("• New topics: ${String.format("%.1f", hoursPerWeek * 0.5)} hours")
            appendLine("• Practice: ${String.format("%.1f", hoursPerWeek * 0.3)} hours")
            appendLine("• Revision: ${String.format("%.1f", hoursPerWeek * 0.2)} hours")
        }
    }

    private fun generateDailyTimetable(subjects: List<String>): String {
        val hoursPerSubject = studyHoursPerDay / subjects.size
        val prioritySubjects = subjects.take(3)

        return buildString {
            appendLine("⏰ Your Daily Study Schedule (${String.format("%.1f", studyHoursPerDay)} hours)")
            appendLine()
            appendLine("🌅 Morning Session (6:00 AM - 9:00 AM):")
            appendLine("   • 6:00-6:30: Wake up & Exercise")
            appendLine("   • 6:30-7:30: Study ${prioritySubjects[0]} (${String.format("%.1f", hoursPerSubject)} hour)")
            appendLine("   • 7:30-8:30: Study ${prioritySubjects[1]} (${String.format("%.1f", hoursPerSubject)} hour)")
            appendLine("   • 8:30-9:00: Breakfast & College prep")
            appendLine()
            appendLine("📚 College Hours (9:00 AM - 5:00 PM):")
            appendLine("   • Attend all lectures actively")
            appendLine("   • Take notes and clarify doubts")
            appendLine("   • Utilize free periods for revision")
            appendLine()
            appendLine("🌙 Evening Session (5:00 PM - 10:00 PM):")
            appendLine("   • 5:00-6:00: Rest & Refresh")
            appendLine("   • 6:00-7:00: Practice problems (${prioritySubjects[0]})")
            appendLine("   • 7:00-8:00: Study ${prioritySubjects[2]} (${String.format("%.1f", hoursPerSubject)} hour)")

            if (subjects.size > 3) {
                appendLine("   • 8:00-9:00: Revise remaining subjects")
            }

            appendLine("   • 9:00-10:00: Plan next day & Relax")
            appendLine()
            appendLine("💤 Night Routine:")
            appendLine("   • 10:00 PM: Stop studying")
            appendLine("   • 10:00-10:30: Light reading/Meditation")
            appendLine("   • 10:30 PM: Sleep (7-8 hours essential)")
        }
    }

    private fun generateWeeklyPlan(subjects: List<String>): String {
        val prioritySubjects = subjects.take(4)

        return buildString {
            appendLine("📅 Weekly Study Rotation:")
            appendLine()
            appendLine("Monday - Wednesday:")
            appendLine("   • Focus: ${prioritySubjects[0]}")
            appendLine("   • Complete 2 chapters")
            appendLine("   • Solve 30 problems")
            appendLine()
            appendLine("Thursday - Saturday:")
            appendLine("   • Focus: ${prioritySubjects[1]}")
            appendLine("   • Complete 2 chapters")
            appendLine("   • Solve 30 problems")
            appendLine()
            appendLine("Sunday:")
            appendLine("   • Morning: Revise all weekly topics")
            appendLine("   • Afternoon: Take mock test")
            appendLine("   • Evening: Analyze mistakes")
            appendLine("   • Night: Plan next week")
            appendLine()
            appendLine("📌 Daily Minimum Targets:")
            appendLine("   • Learn 1 new concept")
            appendLine("   • Solve 10 problems")
            appendLine("   • Revise 2 previous topics")
            appendLine("   • Practice 10 formulas")
        }
    }

    private fun generateStudyTips(): String {
        return buildString {
            appendLine("💡 Exam Success Tips:")
            appendLine()
            appendLine("🎯 Smart Study Techniques:")
            appendLine("• Pomodoro: 50 min study + 10 min break")
            appendLine("• Active recall - test yourself")
            appendLine("• Spaced repetition for revision")
            appendLine("• Teach concepts to others")
            appendLine()
            appendLine("📱 Best Apps for Study:")
            appendLine("• Forest - Beat phone addiction")
            appendLine("• Anki - Flashcards for revision")
            appendLine("• Notion - Organize notes")
            appendLine("• YouTube - NPTEL video lectures")
            appendLine()
            appendLine("⚠️ Common Mistakes to Avoid:")
            appendLine("• Don't study new topics last week")
            appendLine("• Avoid all-nighters before exam")
            appendLine("• Don't skip sleep/meals")
            appendLine("• Stop comparing with others")
            appendLine()
            appendLine("✅ Last Week Strategy:")
            appendLine("• Only revise what you know")
            appendLine("• Take 1 full test daily")
            appendLine("• Stay calm and confident")
            appendLine("• Reach exam center early")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}