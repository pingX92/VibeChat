package com.example.vibechat.model

data class Message(
    val text: String,
    // true == user, false == AI
    val isUser: Boolean
)