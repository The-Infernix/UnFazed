package com.example.unfazed.data

data class UniversityKnowledge(
    val id: String,
    val category: String, // "Exam", "Registration", "Library", "Lab", "Process"
    val question: String,
    val answer: String,
    val keywords: List<String>
)

object AndhraUniversityData {
    val knowledgeBase = listOf(
        // Exam Related
        UniversityKnowledge(
            id = "exam_1",
            category = "Exam",
            question = "When are the semester exams?",
            answer = "Even Semester exams: April-May, Odd Semester exams: November-December. Mid exams are usually in March and September.",
            keywords = listOf("exam", "semester exam", "dates", "schedule")
        ),
        UniversityKnowledge(
            id = "exam_2",
            category = "Exam",
            question = "How to apply for revaluation?",
            answer = "Revaluation applications can be submitted online through student portal within 10 days of results declaration. Fee: ₹500 per paper.",
            keywords = listOf("revaluation", "paper checking", "results")
        ),
        UniversityKnowledge(
            id = "exam_3",
            category = "Exam",
            question = "What is the passing marks?",
            answer = "Minimum 40% in both internal (20/50) and external (24/60) exams separately. Aggregate 40% required.",
            keywords = listOf("passing marks", "minimum marks", "pass percentage")
        ),

        // Registration Related
        UniversityKnowledge(
            id = "reg_1",
            category = "Registration",
            question = "How to register for next semester?",
            answer = "Login to student portal → Registration → Select subjects → Pay fees → Confirm. Deadline is usually 2 weeks before semester starts.",
            keywords = listOf("registration", "subject registration", "enrollment")
        ),
        UniversityKnowledge(
            id = "reg_2",
            category = "Registration",
            question = "What is the registration fee?",
            answer = "Tuition fee: ₹20,000-40,000 per semester depending on branch. Late fee: ₹1000 per week after deadline.",
            keywords = listOf("fee", "registration fee", "tuition fee", "payment")
        ),

        // Library Related
        UniversityKnowledge(
            id = "lib_1",
            category = "Library",
            question = "How to borrow books from library?",
            answer = "Visit Central Library with Student ID. General books: 15 days, Reference books: 7 days. Fine: ₹5/day for delay.",
            keywords = listOf("library", "borrow books", "issue books", "return books")
        ),
        UniversityKnowledge(
            id = "lib_2",
            category = "Library",
            question = "How to access e-library?",
            answer = "Visit digital library portal → Login with student ID → Access IEEE, Springer, ScienceDirect. Remote access available.",
            keywords = listOf("e-library", "digital library", "online journals", "ieee")
        ),

        // Lab Related
        UniversityKnowledge(
            id = "lab_1",
            category = "Lab",
            question = "What are the lab timings?",
            answer = "Computer Labs: 9 AM - 8 PM. Department Labs: 9 AM - 5 PM. Project Lab: 24/7 with prior permission.",
            keywords = listOf("lab timings", "computer lab", "department lab")
        ),
        UniversityKnowledge(
            id = "lab_2",
            category = "Lab",
            question = "How to access A-Hub?",
            answer = "A-Hub (Atal Incubation Centre) is open 10 AM - 6 PM. Register online for membership. Free for students.",
            keywords = listOf("a-hub", "incubation", "startup", "innovation hub")
        ),

        // Academic Processes
        UniversityKnowledge(
            id = "acad_1",
            category = "Process",
            question = "How to apply for transcript?",
            answer = "Submit application at Academic Section. Fee: ₹500 per transcript. Processing time: 7 working days.",
            keywords = listOf("transcript", "marksheet", "certificate")
        ),
        UniversityKnowledge(
            id = "acad_2",
            category = "Process",
            question = "How to apply for bonafide certificate?",
            answer = "Request through student portal or visit HOD office. Fee: ₹100. Takes 1-2 days.",
            keywords = listOf("bonafide", "certificate", "student certificate")
        ),
        UniversityKnowledge(
            id = "acad_3",
            category = "Process",
            question = "How to calculate SGPA/CGPA?",
            answer = "SGPA = Σ(credits × grade points)/Σ credits. CGPA = Σ(SGPA × semester credits)/Σ credits",
            keywords = listOf("sgpa", "cgpa", "gpa calculation", "grades")
        ),

        // Placement Related
        UniversityKnowledge(
            id = "place_1",
            category = "Placement",
            question = "When does placement season start?",
            answer = "Placements begin in August (for final year). Pre-placement talks start from July. Registration deadline: June.",
            keywords = listOf("placement season", "campus placements", "recruitment")
        ),
        UniversityKnowledge(
            id = "place_2",
            category = "Placement",
            question = "What is the eligibility for placements?",
            answer = "Minimum 60% aggregate or 6.5 CGPA. No active backlogs. Good communication skills required.",
            keywords = listOf("placement eligibility", "criteria", "cgpa required")
        ),

        // GATE Related
        UniversityKnowledge(
            id = "gate_1",
            category = "GATE",
            question = "When is GATE exam conducted?",
            answer = "GATE is usually conducted in February first/second week. Application deadline: September-October.",
            keywords = listOf("gate exam", "gate date", "gate schedule")
        ),
        UniversityKnowledge(
            id = "gate_2",
            category = "GATE",
            question = "What is GATE cutoff for IITs?",
            answer = "General category: 30-40 marks (varies by branch). OBC: 27-35, SC/ST: 20-25.",
            keywords = listOf("gate cutoff", "gate score", "iit admission")
        )
    )
}