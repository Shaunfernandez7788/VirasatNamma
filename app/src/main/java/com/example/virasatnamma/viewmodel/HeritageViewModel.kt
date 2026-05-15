package com.example.virasatnamma.viewmodel

import android.app.Application
import com.example.virasatnamma.BuildConfig
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.virasatnamma.data.local.CheckInEntity
import com.example.virasatnamma.data.local.VirasatDatabase
import com.example.virasatnamma.data.model.ChatMessage
import com.example.virasatnamma.data.model.ExpenseEstimate
import com.example.virasatnamma.data.model.HeritageSite
import com.example.virasatnamma.data.model.Hotel
import com.example.virasatnamma.data.repository.GeminiRepository
import com.example.virasatnamma.data.repository.HeritageRepository
import com.example.virasatnamma.data.repository.HotelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HeritageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HeritageRepository
    private val geminiRepository = GeminiRepository()

    // ── Sites ──────────────────────────────────────────────────────────────────
    private val _sites = MutableStateFlow<List<HeritageSite>>(emptyList())
    val sites: StateFlow<List<HeritageSite>> = _sites.asStateFlow()

    // ── Check-ins ──────────────────────────────────────────────────────────────
    private val _checkIns = MutableStateFlow<List<CheckInEntity>>(emptyList())
    val checkIns: StateFlow<List<CheckInEntity>> = _checkIns.asStateFlow()

    // ── Detail state ───────────────────────────────────────────────────────────
    private val _isCheckedIn = MutableStateFlow(false)
    val isCheckedIn: StateFlow<Boolean> = _isCheckedIn.asStateFlow()

    // ── Expense ────────────────────────────────────────────────────────────────
    private val _expenses = MutableStateFlow<Map<String, ExpenseEstimate>>(emptyMap())
    val expenses: StateFlow<Map<String, ExpenseEstimate>> = _expenses.asStateFlow()

    // ── Hotels ─────────────────────────────────────────────────────────────────
    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())
    val hotels: StateFlow<List<Hotel>> = _hotels.asStateFlow()

    // ── Chat ───────────────────────────────────────────────────────────────────
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping: StateFlow<Boolean> = _isAiTyping.asStateFlow()

    // ── Audio ──────────────────────────────────────────────────────────────────
    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

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

    // ── Site helpers ───────────────────────────────────────────────────────────

    fun getSiteById(id: String): HeritageSite? {
        return repository.getSiteById(id)
    }

    // ── Check-in ───────────────────────────────────────────────────────────────

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

    // ── Hotels ─────────────────────────────────────────────────────────────────

    fun loadNearbyHotels(siteId: String) {
        _hotels.value = HotelRepository.getNearbyHotels(siteId)
    }

    fun selectHotel(siteId: String, hotel: Hotel) {
        val current = _expenses.value[siteId] ?: return

        val updated = current.copy(stay = hotel.price)

        _expenses.value = _expenses.value.toMutableMap().apply {
            put(siteId, updated)
        }
    }

    // ── Expense ────────────────────────────────────────────────────────────────

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

        _expenses.value = _expenses.value.toMutableMap().apply {
            put(
                siteId,
                ExpenseEstimate(
                    travel = travel,
                    food = food,
                    stay = stay,
                    misc = misc
                )
            )
        }

        loadNearbyHotels(siteId)
    }

    fun getExpenseForSite(siteId: String): ExpenseEstimate? {
        return _expenses.value[siteId]
    }

    // ── Audio ──────────────────────────────────────────────────────────────────

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

    // ── Chat (Gemini AI) ──────────────────────────────────────────────────────

    fun initChat(siteId: String) {

        val site = getSiteById(siteId)

        _chatMessages.value = listOf(
            ChatMessage(
                text = "Namaskara! 🙏 I'm Virasat, your AI heritage guide for ${site?.name ?: "this site"}. Ask me anything about its history, architecture, visiting tips, or local culture!",
                isUser = false
            )
        )
    }

    fun sendMessage(userText: String, siteId: String) {

        if (userText.isBlank()) return

        android.util.Log.d("GEMINI_DEBUG", "API Key = '${BuildConfig.GEMINI_API_KEY}'")

        val site = getSiteById(siteId)

        // Add user message
        _chatMessages.value =
            _chatMessages.value + ChatMessage(userText, isUser = true)

        _isAiTyping.value = true

        viewModelScope.launch {

            val result = geminiRepository.sendMessage(userText, site)

            result.fold(

                onSuccess = { reply ->

                    _chatMessages.value =
                        _chatMessages.value + ChatMessage(
                            text = reply,
                            isUser = false
                        )
                },

                onFailure = { error ->

                    val errorMsg = when {

                        error.message?.contains("401") == true ||
                                error.message?.contains("403") == true -> {
                            "⚠️ API key issue. Please check your Gemini API key."
                        }

                        error.message?.contains("429") == true -> {
                            "⚠️ Too many requests. Please wait and try again."
                        }

                        error.message?.contains(
                            "timeout",
                            ignoreCase = true
                        ) == true ||
                                error.message?.contains(
                                    "Unable to resolve host",
                                    ignoreCase = true
                                ) == true -> {
                            "⚠️ No internet connection."
                        }

                        else -> {
                            "⚠️ Something went wrong. Please try again."
                        }
                    }

                    _chatMessages.value =
                        _chatMessages.value + ChatMessage(
                            text = errorMsg,
                            isUser = false
                        )
                }
            )

            _isAiTyping.value = false
        }
    }
}