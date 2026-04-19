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
    private lateinit var tvSyllabusBreakdown: TextView

    private var branch = ""
    private var year = ""
    private var selectedSemester = 5
    private var daysLeft = 60
    private var studyHoursPerDay = 4.0f

    // 5th Semester Subjects with detailed topics
    private val semester5Subjects = mapOf(
        "Data Communications & Computer Networks" to listOf(
            "Network Models (OSI, TCP/IP)",
            "Analog/Digital Transmission",
            "Flow & Error Control",
            "Signal Encoding",
            "Multiplexing (FDM, TDM)",
            "Wireless LAN (802.11)",
            "Routing & Congestion Control",
            "IP Protocol & Addressing",
            "TCP & UDP"
        ),
        "Artificial Intelligence" to listOf(
            "State Space Search",
            "BFS, DFS, Hill Climbing",
            "A* & AO* Search",
            "Constraint Satisfaction",
            "Predicate Logic & Resolution",
            "Bayesian Inference",
            "Fuzzy Logic",
            "Semantic Nets & Frames",
            "NLP Fundamentals",
            "Expert Systems"
        ),
        "Compiler Design" to listOf(
            "Compiler Phases",
            "Lexical Analysis & LEX",
            "Syntax Analysis (Top-down)",
            "Syntax Analysis (Bottom-up)",
            "Semantic Analysis",
            "Intermediate Code Generation",
            "Code Optimization",
            "Code Generation",
            "Symbol Tables"
        )
    )

    // Topic weights for prioritization (based on complexity)
    private val topicWeights = mapOf(
        "Data Communications & Computer Networks" to mapOf(
            "Network Models (OSI, TCP/IP)" to 8,
            "Analog/Digital Transmission" to 6,
            "Flow & Error Control" to 9,
            "Signal Encoding" to 7,
            "Multiplexing (FDM, TDM)" to 7,
            "Wireless LAN (802.11)" to 8,
            "Routing & Congestion Control" to 9,
            "IP Protocol & Addressing" to 8,
            "TCP & UDP" to 9
        ),
        "Artificial Intelligence" to mapOf(
            "State Space Search" to 7,
            "BFS, DFS, Hill Climbing" to 6,
            "A* & AO* Search" to 8,
            "Constraint Satisfaction" to 7,
            "Predicate Logic & Resolution" to 9,
            "Bayesian Inference" to 8,
            "Fuzzy Logic" to 7,
            "Semantic Nets & Frames" to 6,
            "NLP Fundamentals" to 8,
            "Expert Systems" to 7
        ),
        "Compiler Design" to mapOf(
            "Compiler Phases" to 6,
            "Lexical Analysis & LEX" to 8,
            "Syntax Analysis (Top-down)" to 9,
            "Syntax Analysis (Bottom-up)" to 9,
            "Semantic Analysis" to 8,
            "Intermediate Code Generation" to 8,
            "Code Optimization" to 7,
            "Code Generation" to 7,
            "Symbol Tables" to 6
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
        supportActionBar?.title = "5th Semester Smart Planner"
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
        tvSyllabusBreakdown = findViewById(R.id.tvSyllabusBreakdown)
    }

    private fun setupListeners() {
        chipGroupSemester.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = findViewById<Chip>(checkedIds[0])
                selectedSemester = when (chip.text.toString()) {
                    "Semester 5" -> 5
                    else -> 5
                }
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
            generateSmartTimetable()
            layoutTimetable.visibility = LinearLayout.VISIBLE
        }
    }

    private fun preSelectSemester() {
        val chipSem5 = findViewById<Chip>(R.id.chipSem5)
        chipSem5.isChecked = true
        selectedSemester = 5
    }

    private fun generateSmartTimetable() {
        val subjects = semester5Subjects.keys.toList()
        val strategy = generateExamStrategy(subjects)  // FIXED: Pass subjects parameter
        val timetable = generateDailyTimetable(subjects)
        val weeklyPlan = generateWeeklyPlan(subjects)
        val tips = generateStudyTips()
        val syllabusBreakdown = generateSyllabusBreakdown()

        tvStrategy.text = strategy
        tvSubjects.text = generateSubjectsWithTopics(subjects)
        tvTimetable.text = timetable
        tvWeeklyPlan.text = weeklyPlan
        tvTips.text = tips
        tvSyllabusBreakdown.text = syllabusBreakdown
    }

    private fun generateSubjectsWithTopics(subjects: List<String>): String {
        return buildString {
            subjects.forEach { subject ->
                appendLine("📖 $subject")
                appendLine("   Topics:")
                semester5Subjects[subject]?.forEach { topic ->
                    appendLine("   • $topic")
                }
                appendLine()
            }
        }
    }

    // FIXED: Added subjects parameter
    private fun generateExamStrategy(subjects: List<String>): String {
        val totalTopics = semester5Subjects.values.flatten().size
        val topicsPerDay = (totalTopics.toFloat() / daysLeft).coerceAtLeast(0.5f)
        val hoursPerTopic = studyHoursPerDay / topicsPerDay

        return buildString {
            appendLine("🎯 5th Semester Exam Strategy")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine()
            appendLine("📊 Your Timeline:")
            appendLine("• Days left: $daysLeft days")
            appendLine("• Study hours/day: ${String.format("%.1f", studyHoursPerDay)} hours")
            appendLine("• Total study time: ${String.format("%.0f", daysLeft * studyHoursPerDay)} hours")
            appendLine("• Total topics to cover: $totalTopics")
            appendLine("• Topics per day: ${String.format("%.1f", topicsPerDay)}")
            appendLine("• Hours per topic: ${String.format("%.1f", hoursPerTopic)}")
            appendLine()

            when {
                daysLeft > 120 -> {
                    appendLine("📌 Phase 1 (Days 1-${daysLeft - 90}): Deep Learning")
                    appendLine("   • Master all 3 subjects thoroughly")
                    appendLine("   • Focus on understanding concepts")
                    appendLine("   • Solve numerical problems")
                    appendLine()
                    appendLine("📌 Phase 2 (Next 60 days): Practice & Revision")
                    appendLine("   • Solve previous year papers")
                    appendLine("   • Practice important topics")
                    appendLine()
                    appendLine("📌 Phase 3 (Last 30 days): Intensive Revision")
                    appendLine("   • Focus on weak areas")
                    appendLine("   • Take mock tests")
                }
                daysLeft in 91..120 -> {
                    appendLine("📌 Phase 1 (Days 1-${daysLeft - 60}): Complete Syllabus")
                    appendLine("   • Cover all topics systematically")
                    appendLine("   • Practice numericals daily")
                    appendLine()
                    appendLine("📌 Phase 2 (Last 60 days): Revision & Testing")
                    appendLine("   • Solve 5 years PYQs")
                    appendLine("   • Take weekly mock tests")
                }
                daysLeft in 61..90 -> {
                    appendLine("📌 Phase 1 (Days 1-${daysLeft - 30}): Rapid Coverage")
                    appendLine("   • Cover high-weightage topics first")
                    appendLine("   • Focus on important concepts")
                    appendLine()
                    appendLine("📌 Phase 2 (Last 30 days): Smart Revision")
                    appendLine("   • Solve PYQs (last 3 years)")
                    appendLine("   • Take mock tests every 3 days")
                }
                daysLeft in 31..60 -> {
                    appendLine("⚠️ Intensive Preparation Mode:")
                    appendLine("• Focus only on high-priority topics")
                    appendLine("• Solve problems from each unit")
                    appendLine("• Take mock tests every 2 days")
                    appendLine("• Revise formulas daily")
                }
                daysLeft <= 30 -> {
                    appendLine("🚨 CRITICAL - Last Minute Strategy:")
                    appendLine("• ONLY revise what you already know")
                    appendLine("• Solve 1 full test daily")
                    appendLine("• Don't learn new topics")
                    appendLine("• Focus on time management")
                    appendLine("• Review important formulas")
                }
            }

            appendLine()
            appendLine("📊 Weekly Target:")
            appendLine("• Subjects to cover: ${(3.0 * (7.0 / daysLeft)).coerceAtMost(3.0).toInt()}")
            appendLine("• Problems to solve: ${(studyHoursPerDay * 7 * 5).toInt()}")
            appendLine("• Mock tests: ${if (daysLeft > 60) 1 else 2}")
        }
    }

    private fun generateDailyTimetable(subjects: List<String>): String {
        val hoursPerSubject = studyHoursPerDay / subjects.size
        val prioritySubjects = prioritizeSubjects(subjects)

        return buildString {
            appendLine("⏰ Your Personalized Daily Timetable")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine()
            appendLine("🌅 Morning Session (6:00 AM - 9:00 AM):")
            appendLine("   • 6:00-6:30: Wake up & Exercise")
            appendLine("   • 6:30-7:30: ${prioritySubjects[0]} (${String.format("%.1f", hoursPerSubject)} hour)")
            appendLine("   • 7:30-8:30: ${prioritySubjects[1]} (${String.format("%.1f", hoursPerSubject)} hour)")
            appendLine("   • 8:30-9:00: Breakfast & College prep")
            appendLine()
            appendLine("📚 College Hours (9:00 AM - 5:00 PM):")
            appendLine("   • Attend all ${prioritySubjects[0]} lectures")
            appendLine("   • Take notes and clarify doubts")
            appendLine("   • Utilize free periods for ${prioritySubjects[1]} revision")
            appendLine()
            appendLine("🌙 Evening Session (5:00 PM - 10:00 PM):")
            appendLine("   • 5:00-6:00: Rest & Refresh")
            appendLine("   • 6:00-7:00: Practice problems (${prioritySubjects[0]})")
            appendLine("   • 7:00-8:00: ${prioritySubjects[2]} (${String.format("%.1f", hoursPerSubject)} hour)")
            appendLine("   • 8:00-9:00: Solve numericals (${prioritySubjects[1]})")
            appendLine("   • 9:00-10:00: Plan next day & Relax")
            appendLine()
            appendLine("💤 Night Routine:")
            appendLine("   • 10:00 PM: Stop studying")
            appendLine("   • 10:00-10:30: Quick revision of formulas")
            appendLine("   • 10:30 PM: Sleep (7-8 hours essential)")
            appendLine()
            appendLine("📌 Subject Focus Areas:")
            appendLine("   • ${prioritySubjects[0]}: Theory + Numericals")
            appendLine("   • ${prioritySubjects[1]}: Problem Solving")
            appendLine("   • ${prioritySubjects[2]}: Concept Building")
        }
    }

    private fun prioritizeSubjects(subjects: List<String>): List<String> {
        // Prioritize based on complexity and importance
        return when {
            daysLeft <= 30 -> listOf(
                "Compiler Design",
                "Data Communications & Computer Networks",
                "Artificial Intelligence"
            )
            daysLeft <= 60 -> listOf(
                "Compiler Design",
                "Artificial Intelligence",
                "Data Communications & Computer Networks"
            )
            else -> subjects
        }
    }

    private fun generateWeeklyPlan(subjects: List<String>): String {
        val prioritySubjects = prioritizeSubjects(subjects)

        return buildString {
            appendLine("📅 5th Semester Weekly Study Plan")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine()
            appendLine("Monday - Wednesday:")
            appendLine("   📖 Focus: ${prioritySubjects[0]}")
            appendLine("   • Complete 3-4 topics")
            appendLine("   • Solve 20 problems")
            appendLine("   • Revise previous topics")
            appendLine("   • ${getSpecificTopicsForSubject(prioritySubjects[0])}")
            appendLine()
            appendLine("Thursday - Saturday:")
            appendLine("   📖 Focus: ${prioritySubjects[1]}")
            appendLine("   • Complete 3-4 topics")
            appendLine("   • Solve 20 problems")
            appendLine("   • Practice numericals")
            appendLine("   • ${getSpecificTopicsForSubject(prioritySubjects[1])}")
            appendLine()
            appendLine("Sunday:")
            appendLine("   🌅 Morning: ${prioritySubjects[2]} Revision")
            appendLine("   📝 Afternoon: Full Syllabus Mock Test")
            appendLine("   📊 Evening: Analyze mistakes")
            appendLine("   📅 Night: Plan next week's targets")
            appendLine()
            appendLine("📌 Weekly Targets:")
            appendLine("   • Topics to complete: 10-12")
            appendLine("   • Problems to solve: 60-80")
            appendLine("   • Mock tests: 1-2")
            appendLine("   • Formula revision: Daily 15 min")
        }
    }

    private fun getSpecificTopicsForSubject(subject: String): String {
        return when (subject) {
            "Compiler Design" -> "   • Focus: Syntax Analysis, Code Optimization"
            "Artificial Intelligence" -> "   • Focus: Search Algorithms, NLP, Expert Systems"
            "Data Communications & Computer Networks" -> "   • Focus: TCP/IP, Routing, Network Models"
            else -> "   • Cover all important topics"
        }
    }

    private fun generateSyllabusBreakdown(): String {
        return buildString {
            appendLine("📚 Detailed Syllabus Breakdown")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine()

            semester5Subjects.forEach { (subject, topics) ->
                appendLine("🔹 $subject")
                appendLine("   ━━━━━━━━━━━━━━━━━━━")
                topics.forEachIndexed { index, topic ->
                    val weight = topicWeights[subject]?.get(topic) ?: 5
                    val priority = when {
                        weight >= 8 -> "⭐ HIGH PRIORITY"
                        weight >= 6 -> "🟡 Medium Priority"
                        else -> "🟢 Normal"
                    }
                    appendLine("   ${index + 1}. $topic - $priority")
                }
                appendLine()
            }

            appendLine("💡 Exam Pattern Tips:")
            appendLine("   • DCCN: Theory 60% + Numericals 40%")
            appendLine("   • AI: Concepts 70% + Problem Solving 30%")
            appendLine("   • CD: Technical 80% + Application 20%")
        }
    }

    private fun generateStudyTips(): String {
        return buildString {
            appendLine("💡 Smart Study Tips for 5th Semester")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine()
            appendLine("🎯 Subject-Wise Strategy:")
            appendLine()
            appendLine("📡 Data Communications & Computer Networks:")
            appendLine("   • Draw OSI/TCP/IP diagrams daily")
            appendLine("   • Practice numerical problems")
            appendLine("   • Use mnemonics for protocols")
            appendLine()
            appendLine("🤖 Artificial Intelligence:")
            appendLine("   • Implement search algorithms on paper")
            appendLine("   • Practice logic problems")
            appendLine("   • Understand real-world applications")
            appendLine()
            appendLine("⚙️ Compiler Design:")
            appendLine("   • Practice parsing examples")
            appendLine("   • Draw compiler phases diagram")
            appendLine("   • Solve optimization problems")
            appendLine()
            appendLine("📱 Best Resources:")
            appendLine("   • DCCN: Tanenbaum, Kurose")
            appendLine("   • AI: Russell & Norvig")
            appendLine("   • CD: Aho, Ullman (Dragon Book)")
            appendLine("   • YouTube: NPTEL lectures")
            appendLine()
            appendLine("⚠️ Common Mistakes to Avoid:")
            appendLine("   • Don't skip numerical problems in DCCN")
            appendLine("   • Don't memorize AI algorithms without understanding")
            appendLine("   • Don't ignore compiler phases sequence")
            appendLine("   • Avoid last-minute learning in CD")
            appendLine()
            appendLine("✅ Last Week Strategy:")
            appendLine("   • Only revise what you know")
            appendLine("   • Focus on high-weightage topics")
            appendLine("   • Practice time management")
            appendLine("   • Stay calm and confident")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}