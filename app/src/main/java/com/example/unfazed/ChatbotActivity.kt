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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// --- Data Classes for Chat & OpenRouter API ---
data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class OpenRouterRequest(
    val model: String,
    val messages: List<ApiMessage>
)

data class ApiMessage(
    val role: String,
    val content: String
)

data class OpenRouterResponse(
    val choices: List<Choice>?
)

data class Choice(
    val message: ApiMessage?
)

class ChatbotActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: TextInputEditText
    private lateinit var btnSend: MaterialButton
    private lateinit var tvTyping: TextView
    private lateinit var toolbar: Toolbar

    private val messages = mutableListOf<ChatMessage>()
    private lateinit var messageAdapter: MessageAdapter

    // User Context
    private var userName = "Student"
    private var userBranch = "Engineering"
    private var userYear = "1st Year"
    private var userGoal = "Career"

    // Network Client
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        // Get contextual data passed from Dashboard
        userName = intent.getStringExtra("userName") ?: "Student"
        userBranch = intent.getStringExtra("userBranch") ?: "Engineering"
        userYear = intent.getStringExtra("userYear") ?: "1st Year"
        userGoal = intent.getStringExtra("userGoal") ?: "Career"

        initViews()
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()

        // Send the initial welcome message
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
        supportActionBar?.title = "Unfazed AI Mentor"
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messages)
        rvMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true // Keeps the newest messages at the bottom
        }
        rvMessages.adapter = messageAdapter
    }

    private fun setupClickListeners() {
        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendUserMessage(message)
                etMessage.text?.clear()
                generateAIResponse(message)
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

    private fun generateAIResponse(userPrompt: String) {
        tvTyping.visibility = View.VISIBLE
        rvMessages.scrollToPosition(messages.size - 1)

        val apiKey = BuildConfig.OPENROUTER_API_KEY

        // 🧠 INJECTING THE ANDHRA UNIVERSITY KNOWLEDGE BASE INTO THE AI
        val systemPrompt = """
            You are 'Unfazed AI', an elite academic and career advisor exclusively for students at Andhra University (AUCE).
            You are talking to $userName, who is a $userYear student studying $userBranch, with a primary goal of $userGoal.
            
            Rules:
            1. Always tailor your advice specifically to their branch ($userBranch) and goal ($userGoal).
            2. Keep answers concise, actionable, and highly structured using bullet points and emojis.
            3. STRICTLY use the following real Andhra University facilities and context when giving advice:
               - Libraries: Dr. V.S. Krishna Central Library (400k books, 24/7 reading room), AU Cyber Laboratory.
               - Innovation/Startups: a-hub (AU Incubation Council at North Campus), NASSCOM Centre of Excellence (IoT & AI).
               - Research: Centre for Cyber Security/AI-ML, Nanotechnology CoE, Advanced Analytical Lab (SEM & Micropulse Lidar).
               - Support & Wellness: Psychological Services (call 2710031 or 2844430), Training & Placement Cell, Anti-Ragging Cell, Yoga Village.
               - Labs: DigiFac Labs for high-performance computing, Core Labs.
            4. If they ask about startups, route them to a-hub or NASSCOM. If they ask about mental health, give the Psychological Services number. If they ask about research/GATE, route them to the Central Library or Research Centres.
        """.trimIndent()

        // Construct the OpenRouter payload
        val requestData = OpenRouterRequest(
            model = "openrouter/elephant-alpha",
            messages = listOf(
                ApiMessage(role = "system", content = systemPrompt),
                ApiMessage(role = "user", content = userPrompt)
            )
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val body = gson.toJson(requestData).toRequestBody(mediaType)

                val request = Request.Builder()
                    .url("https://openrouter.ai/api/v1/chat/completions")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("HTTP-Referer", "https://unfazed.app")
                    .addHeader("X-Title", "Unfazed Student OS")
                    .post(body)
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val parsedResponse = gson.fromJson(responseBody, OpenRouterResponse::class.java)
                    val reply = parsedResponse.choices?.firstOrNull()?.message?.content

                    withContext(Dispatchers.Main) {
                        if (!reply.isNullOrEmpty()) {
                            addBotMessage(reply)
                        } else {
                            tvTyping.visibility = View.GONE
                            addBotMessage("I received an empty response. Let's try that again.")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        tvTyping.visibility = View.GONE
                        addBotMessage("API Error: ${response.code} - Make sure your OpenRouter API key is correct.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvTyping.visibility = View.GONE
                    addBotMessage("Network error. Please check your connection. 🔌")
                }
                e.printStackTrace()
            }
        }
    }

    private fun getWelcomeMessage(): String {
        return """
            👋 Hey $userName!
            
            I'm Unfazed AI, your personal Andhra University intelligence engine. I know all about AU's campus—from the Central Library to the NASSCOM Centre of Excellence!
            
            I see you are focusing on $userGoal in $userBranch.
            
            💡 Try asking me:
            • "How can I start a company at a-hub?"
            • "What research centers are available for my branch?"
            • "How do I prepare for my goals using campus resources?"
            
            What's on your mind?
        """.trimIndent()
    }

    // --- ADAPTER CLASS ---
    inner class MessageAdapter(private val messages: List<ChatMessage>) :
        RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_message, parent, false)
            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            holder.bind(messages[position])
        }

        override fun getItemCount() = messages.size

        inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cvMessage: CardView = itemView.findViewById(R.id.cvMessage)
            private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
            private val tvTime: TextView = itemView.findViewById(R.id.tvTime)

            fun bind(message: ChatMessage) {
                tvMessage.text = message.message
                val layoutParams = cvMessage.layoutParams as ViewGroup.MarginLayoutParams

                if (message.isUser) {
                    cvMessage.setCardBackgroundColor(Color.parseColor("#6C63FF"))
                    tvMessage.setTextColor(Color.WHITE)
                    layoutParams.marginStart = 120
                    layoutParams.marginEnd = 16
                } else {
                    cvMessage.setCardBackgroundColor(Color.WHITE)
                    tvMessage.setTextColor(Color.BLACK)
                    layoutParams.marginStart = 16
                    layoutParams.marginEnd = 120
                }
                cvMessage.layoutParams = layoutParams

                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                tvTime.text = timeFormat.format(Date(message.timestamp))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}