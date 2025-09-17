package com.example.vibechat.data.repository

import com.example.vibechat.data.remote.ChatRequest
import com.example.vibechat.data.remote.MessageParam
import com.example.vibechat.data.remote.OpenAiApi

class ChatRepository(
    private val api: OpenAiApi,
    private val deployment: String,
    private val apiVersion: String
) {
    suspend fun sendMessage(userMessage: String): String {
        val request = ChatRequest(
            messages = listOf(
                MessageParam("system", "You are a helpful assistant."),
                MessageParam("user", userMessage)
            )
        )

        val response = api.getChatCompletion(deployment, apiVersion, request)
        return response.choices.firstOrNull()?.message?.content ?: "(no response)"
    }
}