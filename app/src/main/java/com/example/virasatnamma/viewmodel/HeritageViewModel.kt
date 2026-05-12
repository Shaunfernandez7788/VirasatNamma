package com.example.virasatnamma.viewmodel

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasatnamma.data.local.CheckInEntity
import com.example.virasatnamma.data.local.VirasatDatabase
import com.example.virasatnamma.data.model.*
import com.example.virasatnamma.data.repository.HeritageRepository
import com.example.virasatnamma.data.repository.HotelRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HeritageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HeritageRepository

    // ── Sites ─────────────────────────────────────────────
    private val _sites = MutableStateFlow<List<HeritageSite>>(emptyList())
    val sites: StateFlow<List<HeritageSite>> = _sites.asStateFlow()

    // ── Check-ins ─────────────────────────────────────────
    private val _checkIns = MutableStateFlow<List<CheckInEntity>>(emptyList())
    val checkIns: StateFlow<List<CheckInEntity>> = _checkIns.asStateFlow()

    // ── Detail state ──────────────────────────────────────
    private val _isCheckedIn = MutableStateFlow(false)
    val isCheckedIn: StateFlow<Boolean> = _isCheckedIn.asStateFlow()

    // ── Expense ───────────────────────────────────────────
    private val _expenses =
        MutableStateFlow<Map<String, ExpenseEstimate>>(emptyMap())

    val expenses: StateFlow<Map<String, ExpenseEstimate>> =
        _expenses.asStateFlow()

    // ── Hotels ────────────────────────────────────────────
    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())
    val hotels: StateFlow<List<Hotel>> = _hotels.asStateFlow()

    init {
        val db = VirasatDatabase.getDatabase(application)
        repository = HeritageRepository(db.checkInDao())

        _sites.value = repository.getAllSites()

        viewModelScope.launch {
            repository.getAllCheckIns().collect {
                _checkIns.value = it
            }
        }
    }

    // ---------------- HOTELS ----------------

    fun loadNearbyHotels(siteId: String) {
        _hotels.value = HotelRepository.getNearbyHotels()
    }

    fun selectHotel(siteId: String, hotel: Hotel) {
        val current = _expenses.value[siteId] ?: return
        val updated = current.copy(stay = hotel.price)

        _expenses.value = _expenses.value.toMutableMap().apply {
            put(siteId, updated)
        }
    }

    // ---------------- EXPENSE ----------------

    fun calculateExpense(siteId: String, budget: String) {
        val site = getSiteById(siteId) ?: return

        val travel = site.distance * 5

        val food = when (budget) {
            "Low" -> 200.0
            "Medium" -> 500.0
            else -> 1000.0
        }

        val stay = when (budget) {
            "Low" -> 800.0
            "Medium" -> 1500.0
            else -> 3000.0
        }

        val misc = 200.0

        val estimate = ExpenseEstimate(travel, food, stay, misc)

        _expenses.value = _expenses.value.toMutableMap().apply {
            put(siteId, estimate)
        }

        loadNearbyHotels(siteId)
    }

    fun getExpenseForSite(siteId: String): ExpenseEstimate? {
        return _expenses.value[siteId]
    }

    // ---------------- SITE ----------------

    fun getSiteById(id: String): HeritageSite? =
        repository.getSiteById(id)

    // ---------------- CHECK-IN ----------------

    fun loadCheckInStatus(siteId: String) {
        viewModelScope.launch {
            _isCheckedIn.value = repository.isCheckedIn(siteId)
        }
    }

    fun checkIn(site: HeritageSite) {
        viewModelScope.launch {
            repository.checkIn(site)
            _isCheckedIn.value = true
        }
    }

    fun updateCheckIn(checkIn: CheckInEntity) {
        viewModelScope.launch {
            repository.updateCheckIn(checkIn)
        }
    }

    fun deleteCheckIn(checkIn: CheckInEntity) {
        viewModelScope.launch {
            repository.deleteCheckIn(checkIn)
        }
    }

    // ---------------- AUDIO ----------------

    private var mediaPlayer: MediaPlayer? = null
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    fun toggleAudio(audioResId: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplication(), audioResId)

            mediaPlayer?.setOnCompletionListener {
                _isPlaying.value = false
                releaseAudio()
            }
        }

        if (_isPlaying.value) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }

        _isPlaying.value = !_isPlaying.value
    }

    fun releaseAudio() {
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
    }

    override fun onCleared() {
        super.onCleared()
        releaseAudio()
    }

    // ---------------- CHAT ----------------

    private val _chatMessages =
        MutableStateFlow<List<ChatMessage>>(emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> =
        _chatMessages.asStateFlow()

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping: StateFlow<Boolean> = _isAiTyping.asStateFlow()

    fun initChat(siteId: String) {
        val site = getSiteById(siteId)

        _chatMessages.value = listOf(
            ChatMessage(
                text = "Namaskara! 🙏 I'm your guide for ${site?.name ?: "this site"}",
                isUser = false
            )
        )
    }

    fun sendMessage(userText: String, siteId: String) {
        if (userText.isBlank()) return

        val site = getSiteById(siteId)

        _chatMessages.value += ChatMessage(userText, true)
        _isAiTyping.value = true

        viewModelScope.launch {
            delay(1200)

            val response = generateMockResponse(userText, site)

            _chatMessages.value += ChatMessage(response, false)
            _isAiTyping.value = false
        }
    }

    private fun generateMockResponse(
        query: String,
        site: HeritageSite?
    ): String {
        val q = query.lowercase()
        val name = site?.name ?: "this heritage site"

        return when {
            q.contains("history") || q.contains("old") || q.contains("built") ->
                "🏛️ $name has a fascinating history spanning several centuries! It was built during an era of great artistic patronage in Karnataka. The site reflects the cultural and religious traditions of its time and remains a living heritage for locals even today."

            q.contains("architecture") || q.contains("design") || q.contains("style") ->
                "🪨 The architecture of $name is a blend of indigenous Kannada craftsmanship with influences from regional dynasties. Notice the intricate stone carvings, the precise proportions, and the symbolic motifs that adorn its walls — each telling a story."

            q.contains("visit") || q.contains("timing") || q.contains("when") || q.contains("open") ->
                "🕐 The best time to visit $name is early morning (6–9 AM) to avoid crowds and the afternoon heat. The site is generally open from sunrise to sunset. Entry is free for Indian nationals on some days. Carry water and wear comfortable footwear!"

            q.contains("food") || q.contains("eat") || q.contains("restaurant") ->
                "🍽️ Near $name you can find local Karnataka cuisine — try the akki rotti, bisi bele bath, and the famous Mysore pak sweet. Local dhabas offer authentic flavors at very reasonable prices. Don't miss the filter coffee!"

            q.contains("tip") || q.contains("suggest") || q.contains("advice") ->
                "💡 Insider tips for $name: Hire a certified local guide for deeper stories. Avoid visiting on public holidays if you dislike crowds. Photography is usually permitted, but check for restricted zones. Respect the sanctity — remove footwear where required."

            q.contains("significance") || q.contains("important") || q.contains("why") ->
                "⭐ $name is significant because it represents the pinnacle of artistic achievement during its era. It is a UNESCO-recognized or state-protected monument that has shaped Karnataka's cultural identity. Thousands of pilgrims and tourists visit each year."

            q.contains("cost") || q.contains("price") || q.contains("ticket") || q.contains("entry") ->
                "🎟️ Entry fees at $name are generally nominal — around ₹15–40 for Indian nationals and ₹300–600 for foreign tourists. Camera charges may apply separately. Children below 15 typically enter free. Always check the ASI website for the latest pricing."

            q.contains("distance") || q.contains("how far") || q.contains("reach") || q.contains("travel") ->
                "🚗 $name is accessible by road, rail, and in some cases air. The nearest major city has regular KSRTC bus services and auto-rickshaws available at the bus stand. For remote sites, hiring a local cab is the most convenient option."

            else ->
                "🙏 That's a great question about $name! This heritage site is truly a gem of Karnataka's rich past. Its stone carvings, sacred spaces, and historical depth make it a must-visit. Is there something specific you'd like to know — history, architecture, visiting tips, or local food?"
        }
    }
}