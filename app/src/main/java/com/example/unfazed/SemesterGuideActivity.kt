package com.example.unfazed

import android.os.Bundle
import android.view.View
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
    private lateinit var tvTimetable: TextView
    private lateinit var tvSyllabusBreakdown: TextView

    private var daysLeft = 60
    private var studyHoursPerDay = 4.0f
    private var selectedSemester = 5

    // 🆕 DYNAMIC SUBJECT DATABASE
    private val allSubjects = mapOf(
        3 to listOf("Data Structures", "Digital Logic Design", "Discrete Mathematics", "OOP with Java"),
        4 to listOf("Operating Systems", "Database Management (DBMS)", "Computer Organization", "Formal Languages (FLAT)"),
        5 to listOf("Compiler Design (CD)", "Artificial Intelligence (AI)", "Computer Networks (DCCN)"),
        6 to listOf("Machine Learning", "Web Technologies", "Software Engineering", "Cryptography")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semester_guide)

        setupToolbar()
        initViews()
        setupListeners()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
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
        tvTimetable = findViewById(R.id.tvTimetable)
        tvSyllabusBreakdown = findViewById(R.id.tvSyllabusBreakdown)
    }

    private fun setupListeners() {
        // 🆕 LISTEN FOR SEMESTER CHANGES
        chipGroupSemester.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = findViewById<Chip>(checkedIds[0])
                // Extract the number from "Sem 5"
                selectedSemester = chip.text.toString().replace("Sem ", "").toInt()
            }
        }

        sliderDaysLeft.addOnChangeListener { _, value, _ ->
            daysLeft = value.toInt()
            tvDaysLeft.text = "$daysLeft days left for exam"
        }

        sliderStudyHours.addOnChangeListener { _, value, _ ->
            studyHoursPerDay = value
            tvStudyHours.text = String.format("%.1f hours/day", studyHoursPerDay)
        }

        btnGenerate.setOnClickListener {
            generateSmartPlan()
            layoutTimetable.visibility = View.VISIBLE
        }
    }

    private fun generateSmartPlan() {
        // Get the subjects for the selected semester, or default to generic if missing
        val currentSubjects = allSubjects[selectedSemester] ?: listOf("Subject 1", "Subject 2", "Subject 3")

        // 1. Generate Strategy Card (The Dark Hero Card)
        tvStrategy.text = buildString {
            append("🎯 SEMESTER $selectedSemester STRATEGY\n")
            append("━━━━━━━━━━━━━━━━━━━━\n")
            val status = if (daysLeft < 30) "🔴 CRITICAL MODE" else "🟢 STEADY PREP"
            append("Status: $status\n\n")

            // Using 25f to prevent the Double/Float mismatch error
            val topicsPerDay = (25f / daysLeft).coerceAtLeast(0.5f)
            append("You need to cover roughly ${topicsPerDay.format(1)} topics per day. ")

            append("With $studyHoursPerDay hours, focus heavily on ${currentSubjects[0]} as it usually requires the most problem-solving practice.")
        }

        // 2. Generate Syllabus Breakdown
        tvSyllabusBreakdown.text = buildString {
            append("📌 SUBJECT PRIORITIES\n\n")
            currentSubjects.forEach { subject ->
                val priority = if (daysLeft < 30) "🔥 HIGH" else "⭐ NORMAL"
                append("• $subject [$priority]\n")
                append("  Focus: Core concepts and PYQs\n\n")
            }
        }

        // 3. Daily Timetable
        tvTimetable.text = buildString {
            append("⏰ OPTIMIZED SCHEDULE\n\n")
            if (currentSubjects.size >= 3) {
                append("06:30 AM - 08:30 AM: Deep Work (${currentSubjects[0]})\n")
                append("06:00 PM - 07:30 PM: Practice (${currentSubjects[1]})\n")
                append("08:30 PM - 09:30 PM: Revision (${currentSubjects[2]})\n\n")
            } else {
                append("Divide your $studyHoursPerDay hours evenly among your subjects.\n\n")
            }
            append("💡 Tip: Use the DigiFac Labs for technical practice.")
        }
    }

    private fun Float.format(digits: Int) = "%.${digits}f".format(this)
}