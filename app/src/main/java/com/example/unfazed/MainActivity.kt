package com.example.unfazed

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class MainActivity : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var spinnerBranch: MaterialAutoCompleteTextView
    private lateinit var spinnerYear: MaterialAutoCompleteTextView
    private lateinit var spinnerGoal: MaterialAutoCompleteTextView
    private lateinit var btnContinue: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupSpinners()
        setupClickListeners()
    }

    private fun initViews() {
        etName = findViewById(R.id.etName)
        spinnerBranch = findViewById(R.id.spinnerBranch)
        spinnerYear = findViewById(R.id.spinnerYear)
        spinnerGoal = findViewById(R.id.spinnerGoal)
        btnContinue = findViewById(R.id.btnContinue)
    }

    private fun setupSpinners() {
        // Branch Spinner
        val branches = listOf("Computer Science (CSE)", "Electronics (ECE)", "Electrical (EEE)", "Mechanical", "Civil")
        val branchAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, branches)
        spinnerBranch.setAdapter(branchAdapter)

        // Year Spinner
        val years = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, years)
        spinnerYear.setAdapter(yearAdapter)

        // Goal Spinner
        val goals = listOf("🎯 Placement/Job", "📚 GATE Exam", "🎓 Higher Studies (MS/PhD)", "💼 Entrepreneurship", "🚀 Startup Career")
        val goalAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, goals)
        spinnerGoal.setAdapter(goalAdapter)
    }

    private fun setupClickListeners() {
        btnContinue.setOnClickListener {
            val name = etName.text.toString().ifEmpty { "Student" }
            val branch = spinnerBranch.text.toString()
            val year = spinnerYear.text.toString()
            val goal = spinnerGoal.text.toString()

            if (branch.isEmpty() || year.isEmpty() || goal.isEmpty()) {
                // Show error - you can add Snackbar here
                return@setOnClickListener
            }

            val intent = Intent(this, DashboardActivity::class.java).apply {
                putExtra("name", name)
                putExtra("branch", branch)
                putExtra("year", year)
                putExtra("goal", goal)
            }
            startActivity(intent)
        }
    }
}