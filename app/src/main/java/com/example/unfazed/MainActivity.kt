package com.example.unfazed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class MainActivity : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var spinnerBranch: MaterialAutoCompleteTextView
    private lateinit var spinnerYear: MaterialAutoCompleteTextView
    private lateinit var spinnerGoal: MaterialAutoCompleteTextView
    private lateinit var btnContinue: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🚀 CHECK IF USER IS ALREADY LOGGED IN
        val prefs = getSharedPreferences("UnfazedPrefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

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
        val branches = listOf("Computer Science (CSE)", "Electronics (ECE)", "Electrical (EEE)", "Mechanical", "Civil")
        spinnerBranch.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, branches))

        val years = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")
        spinnerYear.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, years))

        val goals = listOf("🎯 Placement/Job", "📚 GATE Exam", "🎓 Higher Studies (MS/PhD)", "💼 Entrepreneurship", "🚀 Startup Career")
        spinnerGoal.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, goals))
    }

    private fun setupClickListeners() {
        btnContinue.setOnClickListener {
            val name = etName.text.toString().trim()
            val branch = spinnerBranch.text.toString()
            val year = spinnerYear.text.toString()
            val goal = spinnerGoal.text.toString()

            // ⚠️ UX FIX: Warn the user if they miss a field
            if (name.isEmpty() || branch.isEmpty() || year.isEmpty() || goal.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields to continue", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 💾 SAVE DATA TO DEVICE FOREVER
            val prefs = getSharedPreferences("UnfazedPrefs", Context.MODE_PRIVATE)
            prefs.edit().apply {
                putBoolean("isLoggedIn", true)
                putString("name", name)
                putString("branch", branch)
                putString("year", year)
                putString("goal", goal)
                apply()
            }

            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }
}