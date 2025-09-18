package com.example.vibechat.data.repository

import com.example.vibechat.data.remote.ChatRequest
import com.example.vibechat.data.remote.ContentWithResponse
import com.example.vibechat.data.remote.MessageParam
import com.example.vibechat.data.remote.OpenAiApi
import com.google.gson.Gson

class ChatRepository(
    private val api: OpenAiApi,
    private val deployment: String,
    private val apiVersion: String
) {
    suspend fun sendMessage(userMessage: String): ContentWithResponse? {
        val request = ChatRequest(
            messages = listOf(
                MessageParam("system", """
                    You are a helpful assistant. Always respond naturally to the user's message.
                    In addition, output the detected emotion of your reply in JSON format, like this:
                    {
                      "message": "<your response>",
                      "emotion": "<detected emotion: happy, sad, angry, neutral, excited, fearful>",
                      "holiday": "<detected holiday if relevant, otherwise null>"
                    }

                    Holidays may include: christmas, New Year, lunar New Year, Diwali, Thanksgiving, Halloween, Pride, Valentine’s Day
                    Do not include any text outside the JSON object.
                    """.trimIndent()),
                MessageParam("user", userMessage)
            )
        )

        val response = api.getChatCompletion(deployment, apiVersion, request)
        val content = response.choices.firstOrNull()?.message?.content ?: "(no response)"

        return try {
            Gson().fromJson(content, ContentWithResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }
}