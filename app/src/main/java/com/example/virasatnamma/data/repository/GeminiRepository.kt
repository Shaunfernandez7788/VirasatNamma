package com.example.virasatnamma.data.repository

import com.example.virasatnamma.BuildConfig
import com.example.virasatnamma.data.model.HeritageSite
import com.example.virasatnamma.data.remote.GeminiContent
import com.example.virasatnamma.data.remote.GeminiPart
import com.example.virasatnamma.data.remote.GeminiRequest
import com.example.virasatnamma.data.remote.RetrofitClient

class GeminiRepository {

    private val api = RetrofitClient.geminiApiService
    private val apiKey = BuildConfig.GEMINI_API_KEY.trim()

    suspend fun sendMessage(
        userQuery: String,
        site: HeritageSite?
    ): Result<String> {

        // Fallback if API key missing
        if (apiKey.isBlank()) {
            return Result.success(
                getFallbackResponse(userQuery, site)
            )
        }

        return try {

            val prompt = buildPrompt(userQuery, site)

            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(
                            GeminiPart(text = prompt)
                        )
                    )
                )
            )

            val response = api.generateContent(
                apiKey = apiKey,
                request = request
            )

            val aiText = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?.trim()

            if (!aiText.isNullOrBlank()) {
                Result.success(aiText)
            } else {
                Result.success(
                    getFallbackResponse(userQuery, site)
                )
            }

        } catch (e: Exception) {

            // IMPORTANT:
            // Instead of failing completely,
            // fallback response will still work
            Result.success(
                getFallbackResponse(userQuery, site)
            )
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Prompt Builder
    // ─────────────────────────────────────────────────────────────

    private fun buildPrompt(
        userQuery: String,
        site: HeritageSite?
    ): String {

        val name = site?.name ?: "a Karnataka heritage site"
        val location = site?.location ?: "Karnataka, India"
        val description = site?.description ?: ""

        return """
You are Virasat, an expert AI heritage guide for Karnataka tourism.

Current heritage site:
$name

Location:
$location

About the site:
$description

Instructions:
- Answer clearly and naturally
- Keep answers short and informative
- Use friendly tone
- Include historical and cultural details
- Give travel tips if relevant
- Use emojis occasionally

User question:
$userQuery
        """.trimIndent()
    }

    // ─────────────────────────────────────────────────────────────
    // Fallback Responses
    // ─────────────────────────────────────────────────────────────

    private fun getFallbackResponse(
        query: String,
        site: HeritageSite?
    ): String {

        val q = query.lowercase()
        val name = site?.name ?: "this heritage site"

        return when {

            q.contains("history") ||
                    q.contains("built") ||
                    q.contains("old") -> {

                "🏛️ $name has a rich historical background connected to Karnataka's glorious dynasties. " +
                        "Its architecture and cultural importance continue to attract visitors from around the world."
            }

            q.contains("architecture") ||
                    q.contains("design") -> {

                "🪨 $name is famous for its intricate carvings, detailed stone work, and traditional Karnataka architecture."
            }

            q.contains("best time") ||
                    q.contains("visit") ||
                    q.contains("timing") -> {

                "🕐 The best time to visit $name is early morning or evening when the weather is cooler and ideal for photography."
            }

            q.contains("food") ||
                    q.contains("eat") -> {

                "🍽️ Near $name you can enjoy authentic Karnataka cuisine including dosa, bisi bele bath, and traditional filter coffee."
            }

            q.contains("ticket") ||
                    q.contains("entry") ||
                    q.contains("price") -> {

                "🎟️ Entry fees for $name are usually affordable. Prices may vary for Indian and foreign tourists."
            }

            else -> {

                "🙏 Welcome to $name! Ask me about its history, architecture, travel tips, culture, or nearby attractions."
            }
        }
    }
}