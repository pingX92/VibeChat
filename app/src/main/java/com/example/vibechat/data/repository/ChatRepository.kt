package com.example.vibechat.data.repository

import com.example.vibechat.data.remote.ChatRequest
import com.example.vibechat.data.remote.ContentWithEmotionResponse
import com.example.vibechat.data.remote.MessageParam
import com.example.vibechat.data.remote.OpenAiApi
import com.google.gson.Gson

class ChatRepository(
    private val api: OpenAiApi,
    private val deployment: String,
    private val apiVersion: String
) {
    suspend fun sendMessage(userMessage: String): ContentWithEmotionResponse? {
        val request = ChatRequest(
            messages = listOf(
                MessageParam("system", """
                    You are a helpful assistant. Always respond naturally to the user's message.
                    In addition, output the detected emotion of your reply in JSON format, like this:
                    {
                      "message": "<your response>",
                      "emotion": "<detected emotion: happy, sad, angry, neutral, excited, fearful>"
                    }

                    Do not include any text outside the JSON object.
                    """.trimIndent()),
                MessageParam("user", userMessage)
            )
        )

        val response = api.getChatCompletion(deployment, apiVersion, request)
        val content = response.choices.firstOrNull()?.message?.content ?: "(no response)"

        return try {
            Gson().fromJson(content, ContentWithEmotionResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }
}