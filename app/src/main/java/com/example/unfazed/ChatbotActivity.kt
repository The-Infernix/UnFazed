package com.example.unfazed

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unfazed.data.ChatMessage
import com.example.unfazed.data.UserProfile
import com.example.unfazed.data.UniversityKnowledge
import com.example.unfazed.data.AndhraUniversityData
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class ChatbotActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: TextInputEditText
    private lateinit var btnSend: MaterialButton
    private lateinit var tvTyping: TextView
    private lateinit var toolbar: Toolbar

    private val messages = mutableListOf<ChatMessage>()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var userProfile: UserProfile

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        userProfile = intent.getSerializableExtra("userProfile") as? UserProfile
            ?: UserProfile("Student", "CSE", "2nd Year", "Job", emptyList())

        initViews()
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()

        // Welcome message with RAG capabilities
        addBotMessage(getWelcomeMessage())
    }

    private fun initViews() {
        rvMessages = findViewById(R.id.rvMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        tvTyping = findViewById(R.id.tvTyping)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "AI Assistant"
        toolbar.setTitleTextColor(getColor(R.color.white))
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messages)
        rvMessages.layoutManager = LinearLayoutManager(this)
        rvMessages.adapter = messageAdapter
    }

    private fun setupClickListeners() {
        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendUserMessage(message)
                etMessage.text?.clear()
                generateRAGResponse(message)
            }
        }
    }

    private fun sendUserMessage(message: String) {
        messages.add(ChatMessage(message, true))
        messageAdapter.notifyItemInserted(messages.size - 1)
        rvMessages.scrollToPosition(messages.size - 1)
    }

    private fun addBotMessage(message: String) {
        tvTyping.visibility = View.GONE
        messages.add(ChatMessage(message, false))
        messageAdapter.notifyItemInserted(messages.size - 1)
        rvMessages.scrollToPosition(messages.size - 1)
    }

    private fun showTypingIndicator() {
        tvTyping.visibility = View.VISIBLE
        rvMessages.scrollToPosition(messages.size - 1)
    }

    private fun hideTypingIndicator() {
        tvTyping.visibility = View.GONE
    }

    private fun generateRAGResponse(userMessage: String) {
        showTypingIndicator()

        handler.postDelayed({
            val response = findBestMatch(userMessage)
            addBotMessage(response)
        }, 1000)
    }

    private fun getWelcomeMessage(): String {
        return """
            👋 Namaste ${userProfile.name}!
            
            I'm Unfazed AI Assistant, powered by RAG (Retrieval-Augmented Generation). I have knowledge about:
            
            📚 **Andhra University**
            • Exam schedules & registration
            • Library & e-library access
            • Lab facilities & A-Hub
            • Academic processes
            
            🎯 **Career Guidance**
            • Placements & internships
            • GATE preparation
            • Course recommendations
            • Resume & interview tips
            
            💡 **Try asking me:**
            • "When are semester exams?"
            • "How to access e-library?"
            • "Placement preparation for CSE"
            • "How to apply for transcript?"
            
            How can I help you today?
        """.trimIndent()
    }

    private fun findBestMatch(query: String): String {
        val lowerQuery = query.lowercase()

        // Check for greetings
        if (lowerQuery.matches(Regex(".*\\b(hi|hello|hey|namaste)\\b.*"))) {
            return getGreetingResponse()
        }

        // Check for thanks
        if (lowerQuery.matches(Regex(".*\\b(thank|thanks|dhanyavad)\\b.*"))) {
            return getThanksResponse()
        }

        // Search knowledge base
        val matches = AndhraUniversityData.knowledgeBase.filter { knowledge ->
            knowledge.keywords.any { keyword ->
                lowerQuery.contains(keyword) ||
                        keyword.contains(lowerQuery.take(10)) ||
                        lowerQuery.contains(knowledge.question.lowercase().take(20))
            }
        }

        // Return best match
        return when {
            matches.isNotEmpty() -> formatRAGResponse(matches, query)
            lowerQuery.contains("roadmap") || lowerQuery.contains("plan") || lowerQuery.contains("path") -> getPersonalizedRoadmap()
            lowerQuery.contains("subject") || lowerQuery.contains("syllabus") -> getSubjectGuidance()
            lowerQuery.contains("internship") -> getInternshipGuidance()
            lowerQuery.contains("placement") || lowerQuery.contains("job") -> getPlacementGuidance()
            lowerQuery.contains("gate") -> getGateGuidance()
            lowerQuery.contains("course") || lowerQuery.contains("learn") -> getCourseRecommendations()
            lowerQuery.contains("resume") -> getResumeTips()
            lowerQuery.contains("interview") -> getInterviewTips()
            lowerQuery.contains("hackathon") -> getHackathonInfo()
            lowerQuery.contains("ieee") || lowerQuery.contains("paper") -> getIEEEGuide()
            else -> getDefaultResponse(query)
        }
    }

    private fun formatRAGResponse(matches: List<UniversityKnowledge>, query: String): String {
        return buildString {
            append("📚 **Found relevant information for your query:**\n\n")

            matches.take(3).forEachIndexed { index, match ->
                append("${index + 1}. **${match.question}**\n")
                append("   ${match.answer}\n\n")
            }

            if (matches.size > 3) {
                append("💡 Found ${matches.size} related answers. Ask me more specifically!\n\n")
            }

            append(getContextualSuggestion(query))
            append("\n\n" + getFollowUpPrompt())
        }
    }

    private fun getContextualSuggestion(query: String): String {
        return when {
            query.contains("exam") -> "📝 **Related:** Would you like exam preparation tips or previous year papers?"
            query.contains("library") -> "📖 **Related:** Need help accessing digital resources or finding specific books?"
            query.contains("registration") -> "✅ **Related:** I can help with step-by-step registration process!"
            query.contains("fee") -> "💰 **Related:** Want to know about scholarships or fee payment options?"
            query.contains("lab") -> "🔬 **Related:** Check out our Campus Resources section for lab details!"
            else -> "💡 **Related:** Ask me about deadlines, procedures, or requirements!"
        }
    }

    private fun getGreetingResponse(): String {
        return """
            👋 Hello ${userProfile.name}!
            
            Great to see you! I'm your personal AI assistant for Andhra University.
            
            🎯 **Quick actions you can take:**
            • Ask about university processes
            • Get personalized career roadmap
            • Find campus resources
            • Discover opportunities
            
            What would you like to know about ${userProfile.year} ${userProfile.branch}?
        """.trimIndent()
    }

    private fun getThanksResponse(): String {
        return """
            🎉 You're welcome ${userProfile.name}!
            
            I'm here 24/7 to help you with:
            • 📚 University queries
            • 🎯 Career guidance
            • 🏫 Campus resources
            • 💼 Opportunities
            
            **Keep striving for excellence!** 💪
            
            Is there anything else I can help you with?
        """.trimIndent()
    }

    private fun getPersonalizedRoadmap(): String {
        return buildString {
            append("🗺️ **Personalized Career Roadmap for ${userProfile.year} ${userProfile.branch}**\n\n")

            when {
                userProfile.goal.contains("Placement", ignoreCase = true) -> {
                    append("**🎯 Goal: Placements/Job**\n\n")
                    append("**📌 Immediate Actions (Next 3 months):**\n")
                    when {
                        userProfile.year.contains("1st") -> {
                            append("• Learn programming fundamentals\n")
                            append("• Build logic with basic DSA\n")
                            append("• Maintain CGPA > 8.0\n")
                        }
                        userProfile.year.contains("2nd") -> {
                            append("• Master Data Structures & Algorithms\n")
                            append("• Start competitive programming\n")
                            append("• Build 1-2 projects\n")
                        }
                        userProfile.year.contains("3rd") -> {
                            append("• Advanced DSA & System Design\n")
                            append("• Apply for summer internships\n")
                            append("• Build 2-3 portfolio projects\n")
                        }
                        userProfile.year.contains("4th") -> {
                            append("• Intensive placement preparation\n")
                            append("• Mock interviews practice\n")
                            append("• Apply to dream companies\n")
                        }
                    }

                    append("\n**📚 Skills to Master:**\n")
                    if (userProfile.branch.contains("CSE")) {
                        append("• DSA (LeetCode 200+ problems)\n")
                        append("• DBMS, OS, Networks\n")
                        append("• System Design basics\n")
                        append("• 2-3 programming languages\n")
                    } else {
                        append("• Core branch subjects\n")
                        append("• Technical certifications\n")
                        append("• Aptitude & Reasoning\n")
                        append("• Communication skills\n")
                    }
                }

                userProfile.goal.contains("GATE", ignoreCase = true) -> {
                    append("**🎯 Goal: GATE Examination**\n\n")
                    append("**📌 Preparation Timeline:**\n")
                    append("• Months 1-6: Complete syllabus\n")
                    append("• Months 7-9: Solve PYQs (10 years)\n")
                    append("• Months 10-12: Mock tests & revision\n")

                    append("\n**📚 Recommended Resources:**\n")
                    append("• Standard textbooks (subject-wise)\n")
                    append("• NPTEL video lectures\n")
                    append("• Made Easy/ACE Academy materials\n")
                    append("• GATE Overflow for doubts\n")
                }

                else -> {
                    append("**🎯 Goal: ${userProfile.goal}**\n\n")
                    append("**📌 General Roadmap:**\n")
                    append("• Focus on academic excellence (8+ CGPA)\n")
                    append("• Identify your passion and strengths\n")
                    append("• Build relevant skills and projects\n")
                    append("• Network with seniors and alumni\n")
                    append("• Explore opportunities in your field\n")
                }
            }

            append("\n**💡 Weekly Commitment:**\n")
            append("• 2-3 hours daily for skill development\n")
            append("• 1 hour for practice/revision\n")
            append("• Weekend for projects/networking\n")

            append("\n---\n*Want a detailed month-by-month plan? Ask me!*")
        }
    }

    private fun getSubjectGuidance(): String {
        return buildString {
            append("📚 **Subject Guidance for ${userProfile.branch}**\n\n")

            append("**🔥 Core Subjects to Focus:**\n")
            when {
                userProfile.branch.contains("CSE") -> {
                    append("• Data Structures & Algorithms\n")
                    append("• Operating Systems\n")
                    append("• Database Management Systems\n")
                    append("• Computer Networks\n")
                    append("• Object Oriented Programming\n")
                }
                userProfile.branch.contains("ECE") -> {
                    append("• Analog & Digital Electronics\n")
                    append("• Signals & Systems\n")
                    append("• Communication Systems\n")
                    append("• VLSI Design\n")
                    append("• Microprocessors\n")
                }
                userProfile.branch.contains("EEE") -> {
                    append("• Power Systems\n")
                    append("• Control Systems\n")
                    append("• Electrical Machines\n")
                    append("• Power Electronics\n")
                    append("• Circuit Theory\n")
                }
                else -> {
                    append("• Engineering Mathematics\n")
                    append("• Core branch subjects\n")
                    append("• Technical communication\n")
                }
            }

            append("\n**📖 Best Study Resources:**\n")
            append("• NPTEL video lectures (free)\n")
            append("• Standard textbooks by renowned authors\n")
            append("• Previous year question papers\n")
            append("• Online platforms (Coursera, edX)\n")

            append("\n**💡 Study Tips:**\n")
            append("• Study 2 hours daily per subject\n")
            append("• Practice numericals regularly\n")
            append("• Join study groups for discussions\n")
            append("• Take weekly self-assessments\n")

            append("\n*Need help with a specific subject? Ask me!*")
        }
    }

    private fun getPlacementGuidance(): String {
        return buildString {
            append("💼 **Placement Preparation Guide**\n\n")

            append("**📌 Technical Preparation:**\n")
            if (userProfile.branch.contains("CSE")) {
                append("• DSA: Solve 150+ LeetCode problems\n")
                append("• Core CS subjects revision\n")
                append("• System Design for experienced roles\n")
                append("• Language-specific questions\n")
            } else {
                append("• Core branch subjects mastery\n")
                append("• Technical certifications\n")
                append("• Industry-specific knowledge\n")
            }

            append("\n**📌 Aptitude Preparation:**\n")
            append("• Quantitative Aptitude (Speed Maths)\n")
            append("• Logical Reasoning (Puzzles)\n")
            append("• Verbal Ability (English)\n")

            append("\n**📌 Interview Preparation:**\n")
            append("• Mock interviews with peers\n")
            append("• HR questions preparation\n")
            append("• Company-specific research\n")
            append("• Portfolio/GitHub showcase\n")

            if (userProfile.year.contains("3rd") || userProfile.year.contains("4th")) {
                append("\n⚠️ **URGENT:** Placement season approaching! Start preparation now!\n")
            } else {
                append("\n✅ **Start early to stay ahead!** Begin preparation today.\n")
            }

            append("\n*Want company-specific preparation tips?*")
        }
    }

    private fun getGateGuidance(): String {
        return buildString {
            append("📚 **GATE Exam Comprehensive Guide**\n\n")

            append("**📅 Important Dates:**\n")
            append("• Application: September-October\n")
            append("• Exam: February (first/second week)\n")
            append("• Results: March\n")

            append("\n**📊 Expected Cutoffs (General):**\n")
            when {
                userProfile.branch.contains("CSE") -> append("• CSE: 30-35 marks (out of 100)\n")
                userProfile.branch.contains("ECE") -> append("• ECE: 35-40 marks\n")
                userProfile.branch.contains("EEE") -> append("• EEE: 32-38 marks\n")
                else -> append("• Varies by branch\n")
            }

            append("\n**📚 Subject-wise Weightage:**\n")
            append("• Engineering Mathematics: 15%\n")
            append("• Core subjects: 70%\n")
            append("• General Aptitude: 15%\n")

            append("\n**🎯 Preparation Strategy:**\n")
            append("• Complete syllabus by December\n")
            append("• Solve 10 PYQs daily\n")
            append("• Take weekly mock tests\n")
            append("• Analyze mistakes thoroughly\n")

            append("\n**📖 Best Resources:**\n")
            append("• Standard textbooks (subject-wise)\n")
            append("• NPTEL video lectures\n")
            append("• GATE Previous Year Solved Papers\n")
            append("• Online test series (Made Easy, ACE)\n")

            append("\n*Need a weekly study schedule?*")
        }
    }

    private fun getCourseRecommendations(): String {
        return buildString {
            append("🎓 **Recommended Courses for ${userProfile.year} ${userProfile.branch}**\n\n")

            append("**📊 Based on your goal: ${userProfile.goal}**\n\n")

            append("**🔥 Free Courses (NPTEL):**\n")
            when {
                userProfile.branch.contains("CSE") -> {
                    append("• Programming in C++\n")
                    append("• Data Structures & Algorithms\n")
                    append("• Database Management Systems\n")
                }
                userProfile.branch.contains("ECE") -> {
                    append("• Digital Circuits\n")
                    append("• Signals & Systems\n")
                    append("• Communication Engineering\n")
                }
                else -> {
                    append("• Subject-specific NPTEL courses\n")
                }
            }

            append("\n**💰 Paid Platforms (Worth it):**\n")
            append("• Coursera: Financial aid available\n")
            append("• Udemy: Wait for ₹399 sales\n")
            append("• Great Learning: Free certificates\n")
            append("• LinkedIn Learning: College access\n")

            append("\n**🎯 Platform-Specific for Placements:**\n")
            append("• LeetCode: DSA practice\n")
            append("• CodeChef: Competitive coding\n")
            append("• GeeksforGeeks: Interview prep\n")

            append("\n*Want links to specific courses?*")
        }
    }

    private fun getInternshipGuidance(): String {
        return buildString {
            append("💼 **Internship Guide for ${userProfile.year} Student**\n\n")

            append("**🌟 Where to Find Internships:**\n")
            append("• Internshala: Beginner friendly\n")
            append("• LinkedIn: Professional roles\n")
            append("• Company career pages\n")
            append("• College placement cell\n")
            append("• Wellfound (formerly AngelList)\n")

            append("\n**📝 Application Tips:**\n")
            append("• Resume: One page, ATS-friendly\n")
            append("• Cover letter: Customize each\n")
            append("• Portfolio/GitHub: Show work\n")
            append("• LinkedIn: Complete profile\n")

            append("\n**🎯 Best Time to Apply:**\n")
            append("• Summer internships: Jan-Feb\n")
            append("• Winter internships: Oct-Nov\n")
            append("• Year-round: Check weekly\n")

            append("\n**💰 Stipend Range:**\n")
            append("• Startups: ₹10k-25k/month\n")
            append("• MNCs: ₹25k-60k/month\n")
            append("• Top tech: ₹80k-1.5L/month\n")

            append("\n*Want help with resume or portfolio?*")
        }
    }

    private fun getResumeTips(): String {
        return """
            📄 **Resume Tips for Students**
            
            ✅ **DO's:**
            • Keep it one page (for students)
            • Quantify achievements (e.g., "Improved efficiency by 20%")
            • Tailor to job description
            • Use action verbs (Developed, Created, Implemented)
            • Include GitHub/LinkedIn/Portfolio
            • Add technical skills section
            • Showcase projects with links
            
            ❌ **DON'Ts:**
            • No fancy designs or columns
            • Avoid generic phrases ("Hardworking", "Team player")
            • Don't lie about skills
            • Remove school achievements (after 2nd year)
            • No irrelevant personal info
            • Avoid spelling/grammar errors
            
            🎨 **Best Templates:**
            • Overleaf (LaTeX) - Professional
            • Novoresume - Modern
            • FlowCV - Creative
            • Canva - Design options
            
            📊 **Section Order:**
            1. Contact & Links
            2. Education
            3. Technical Skills
            4. Projects (2-3 strong ones)
            5. Internships/Experience
            6. Achievements/Certifications
            
            *Want a resume review checklist?*
        """.trimIndent()
    }

    private fun getInterviewTips(): String {
        return """
            💼 **Interview Preparation Guide**
            
            📌 **Technical Round:**
            • Practice LeetCode (Easy/Medium)
            • Revise core concepts thoroughly
            • Prepare projects explanation (STAR method)
            • Solve company-specific questions
            • Practice on whiteboard
            
            📌 **HR Round Questions:**
            • "Tell me about yourself" (2-min pitch)
            • "Why this company?" (Research required)
            • "Strengths & weaknesses" (Be honest)
            • "Where do you see yourself in 5 years?"
            • "Why should we hire you?"
            
            📌 **Tips for Success:**
            • Research company thoroughly
            • Prepare questions to ask
            • Dress professionally
            • Arrive 10 minutes early
            • Follow up with thank you email
            
            🎯 **Mock Interview Platforms:**
            • Pramp (free peer-to-peer)
            • InterviewBit (company-specific)
            • College seniors (best resource)
            • YouTube mock interviews
            
            *Need practice questions for ${userProfile.branch}?*
        """.trimIndent()
    }

    private fun getHackathonInfo(): String {
        return """
            🏆 **Hackathon Guide for Students**
            
            🌟 **Upcoming Platforms:**
            • Devfolio - Student focused
            • HackerEarth - Corporate events
            • MLH - Global hackathons
            • Unstop (formerly Dare2Compete)
            
            🛠️ **Preparation:**
            • Form a team (2-4 members)
            • Learn a tech stack thoroughly
            • Prepare boilerplate code
            • Have API keys ready
            • Setup GitHub for collaboration
            
            💡 **Winning Tips:**
            • Solve real problems (not just tech)
            • Working prototype > PPT
            • Practice pitch deck
            • Focus on UI/UX
            • Document everything
            
            🎯 **College Hackathons:**
            • Check CSI/IEEE chapters
            • Ask placement cell
            • Follow college social media
            
            *Want to know about upcoming hackathons?*
        """.trimIndent()
    }

    private fun getIEEEGuide(): String {
        return """
            📄 **IEEE Paper Writing & Publication Guide**
            
            📝 **Step 1: Research**
            • Find problem in your domain
            • Read recent IEEE papers
            • Identify research gap
            • Discuss with professor
            
            📝 **Step 2: Implementation**
            • Propose novel solution
            • Run experiments
            • Document results
            • Compare with existing work
            
            📝 **Step 3: Writing**
            • Follow IEEE format strictly
            • Use Overleaf/LaTeX
            • Plagiarism < 15%
            • Get faculty review
            
            📝 **Step 4: Submission**
            • Find suitable conference
            • Check Scopus indexing
            • Submit through IEEE portal
            • Respond to reviews
            
            💡 **Tips for Beginners:**
            • Start with review paper
            • Target student conferences
            • Collaborate with seniors
            • Use IEEE templates
            
            *Need conference recommendations?*
        """.trimIndent()
    }

    private fun getDefaultResponse(query: String): String {
        return """
            🤔 I understand you're asking about "$query"
            
            Here's what I can help with at Andhra University:
            
            📌 **University Topics:**
            • Exam schedules and dates
            • Registration process
            • Fee payment
            • Library access
            • Lab facilities
            
            📌 **Career Guidance:**
            • Placement preparation
            • GATE exam strategy
            • Internship hunting
            • Resume building
            
            📌 **Academic Support:**
            • Subject guidance
            • Semester planning
            • Study tips
            
            💡 **Try asking:**
            • "When are semester exams?"
            • "How to register for next semester?"
            • "How to access e-library?"
            • "Placement preparation for CSE"
            • "GATE preparation roadmap"
            
            Could you rephrase your question? I'll give you the best answer!
        """.trimIndent()
    }

    private fun getFollowUpPrompt(): String {
        return "💬 **Need more help?** Ask me follow-up questions or type:\n• 'More details'\n• 'Next steps'\n• 'Related topics'"
    }

    inner class MessageAdapter(private val messages: List<ChatMessage>) :
        RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_message, parent, false)
            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = messages[position]
            holder.bind(message)
        }

        override fun getItemCount() = messages.size

        inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cvMessage = itemView.findViewById<androidx.cardview.widget.CardView>(R.id.cvMessage)
            private val tvMessage = itemView.findViewById<TextView>(R.id.tvMessage)
            private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)

            fun bind(message: ChatMessage) {
                tvMessage.text = message.message

                val layoutParams = cvMessage.layoutParams as ViewGroup.MarginLayoutParams
                if (message.isUser) {
                    cvMessage.setCardBackgroundColor(getColor(R.color.primary))
                    tvMessage.setTextColor(getColor(R.color.white))
                    layoutParams.marginStart = 80
                    layoutParams.marginEnd = 0
                } else {
                    cvMessage.setCardBackgroundColor(getColor(R.color.surface))
                    tvMessage.setTextColor(getColor(R.color.text_primary))
                    layoutParams.marginStart = 0
                    layoutParams.marginEnd = 80
                }
                cvMessage.layoutParams = layoutParams

                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                tvTime.text = timeFormat.format(Date(message.timestamp))
                tvTime.gravity = if (message.isUser) android.view.Gravity.END else android.view.Gravity.START
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}