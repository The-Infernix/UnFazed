package com.example.unfazed

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider

class SemesterGuideActivity : AppCompatActivity() {

    private lateinit var seekStrength: Slider
    private lateinit var seekWeakness: Slider
    private lateinit var chipGroupSubjects: ChipGroup
    private lateinit var btnGenerate: MaterialButton
    private lateinit var layoutGuide: LinearLayout
    private lateinit var tvStudyPlan: TextView
    private lateinit var tvTimeTable: TextView
    private lateinit var tvTips: TextView
    private lateinit var tvStrengthValue: TextView
    private lateinit var tvWeaknessValue: TextView

    private var branch = ""
    private var year = ""
    private var selectedSubjects = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semester_guide)

        branch = intent.getStringExtra("branch") ?: ""
        year = intent.getStringExtra("year") ?: ""

        setupToolbar()
        initViews()
        setupSubjectChips()
        setupGenerateButton()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Semester Prep Guide"
    }

    private fun initViews() {
        seekStrength = findViewById(R.id.seekStrength)
        seekWeakness = findViewById(R.id.seekWeakness)
        chipGroupSubjects = findViewById(R.id.chipGroupSubjects)
        btnGenerate = findViewById(R.id.btnGenerate)
        layoutGuide = findViewById(R.id.layoutGuide)
        tvStudyPlan = findViewById(R.id.tvStudyPlan)
        tvTimeTable = findViewById(R.id.tvTimeTable)
        tvTips = findViewById(R.id.tvTips)
        tvStrengthValue = findViewById(R.id.strengthValue)
        tvWeaknessValue = findViewById(R.id.weaknessValue)

        // Setup sliders with value indicators
        setupSlider(seekStrength, tvStrengthValue)
        setupSlider(seekWeakness, tvWeaknessValue)
    }

    private fun setupSlider(slider: Slider, valueTextView: TextView) {
        valueTextView.text = "${slider.value.toInt()}%"
        slider.addOnChangeListener { _, value, _ ->
            valueTextView.text = "${value.toInt()}%"
        }
    }

    private fun setupSubjectChips() {
        val subjects = getSubjectsForBranch()
        subjects.forEach { subject ->
            val chip = Chip(this).apply {
                text = subject
                isCheckable = true
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 8, 8, 8)
                }
            }
            chipGroupSubjects.addView(chip)
        }
    }

    private fun getSubjectsForBranch(): List<String> {
        return when {
            branch.contains("CSE") -> listOf(
                "Data Structures", "Algorithms", "Operating Systems",
                "DBMS", "Computer Networks", "Software Engineering",
                "Web Technologies", "Machine Learning"
            )
            branch.contains("ECE") -> listOf(
                "Analog Circuits", "Digital Electronics", "Signals & Systems",
                "Communication Systems", "VLSI Design", "Microprocessors",
                "Control Systems", "Embedded Systems"
            )
            branch.contains("EEE") -> listOf(
                "Power Systems", "Control Systems", "Electrical Machines",
                "Power Electronics", "Circuit Theory", "Measurements",
                "Renewable Energy", "High Voltage Engineering"
            )
            else -> listOf(
                "Engineering Mathematics", "Physics", "Chemistry",
                "Mechanics", "Thermodynamics", "Fluid Mechanics"
            )
        }
    }

    private fun setupGenerateButton() {
        btnGenerate.setOnClickListener {
            val strength = seekStrength.value.toInt()
            val weakness = seekWeakness.value.toInt()

            selectedSubjects.clear()
            for (i in 0 until chipGroupSubjects.childCount) {
                val chip = chipGroupSubjects.getChildAt(i) as Chip
                if (chip.isChecked) {
                    selectedSubjects.add(chip.text.toString())
                }
            }

            generateStudyPlan(strength, weakness)
            layoutGuide.visibility = android.view.View.VISIBLE
        }
    }

    private fun generateStudyPlan(strength: Int, weakness: Int) {
        val studyHours = when {
            strength > 80 -> "4-5 hours daily"
            strength > 60 -> "5-6 hours daily"
            else -> "6-7 hours daily"
        }

        val focus = if (weakness > 70) "Review basics" else "Practice problems"

        tvStudyPlan.text = buildString {
            appendLine("📚 Personalized Study Plan for $year $branch")
            appendLine("\n━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("\n📊 Your Profile:")
            appendLine("• Current Strength: ${strength}%")
            appendLine("• Areas to Improve: ${weakness}%")
            appendLine("• Daily Capacity: $studyHours")
            appendLine("\n🎯 Focus Areas:")
            appendLine("• $focus for weak topics")

            if (selectedSubjects.isNotEmpty()) {
                appendLine("\n📖 Priority Subjects:")
                selectedSubjects.take(3).forEach { subject ->
                    appendLine("• $subject")
                }
            }
        }

        tvTimeTable.text = generateTimeTable(strength)
        tvTips.text = generateStudyTips(strength, weakness)
    }

    private fun generateTimeTable(strength: Int): String {
        return buildString {
            appendLine("⏰ Ideal Daily Schedule:")
            appendLine("\n🌅 Morning (6 AM - 9 AM):")
            appendLine("• 6:00-7:00: Wake up & Exercise")
            appendLine("• 7:00-8:00: Study tough subjects")
            appendLine("• 8:00-9:00: Breakfast & College prep")

            appendLine("\n📚 College Hours (9 AM - 5 PM):")
            appendLine("• Attend all lectures")
            appendLine("• Take notes actively")
            appendLine("• Clarify doubts immediately")

            appendLine("\n🌙 Evening (5 PM - 10 PM):")
            appendLine("• 5:00-6:00: Rest & Refresh")
            appendLine("• 6:00-8:00: Self-study (${if (strength > 70) "Practice" else "Concept review"})")
            appendLine("• 8:00-9:00: Revision")
            appendLine("• 9:00-10:00: Plan next day")

            appendLine("\n💤 Night:")
            appendLine("• 10:00 PM: Sleep (7-8 hours essential)")
        }
    }

    private fun generateStudyTips(strength: Int, weakness: Int): String {
        return buildString {
            appendLine("💡 Smart Study Tips:")

            if (weakness > 70) {
                appendLine("\n🔧 Improvement Strategy:")
                appendLine("• Start with basics - use YouTube/NPTEL")
                appendLine("• Solve 10 problems daily")
                appendLine("• Take weekly mock tests")
                appendLine("• Form study groups for weak subjects")
            }

            appendLine("\n✅ Effective Techniques:")
            appendLine("• Pomodoro: 50 min study + 10 min break")
            appendLine("• Active recall - test yourself")
            appendLine("• Teach concepts to friends")
            appendLine("• Use mind maps for revision")

            if (strength < 50) {
                appendLine("\n🚀 Quick Improvement:")
                appendLine("• Focus on high-weightage topics")
                appendLine("• Solve previous year papers")
                appendLine("• Take faculty guidance")
            }

            appendLine("\n📱 Recommended Apps:")
            appendLine("• Forest - Avoid distractions")
            appendLine("• Anki - Flashcards for revision")
            appendLine("• Notion - Organize notes")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}