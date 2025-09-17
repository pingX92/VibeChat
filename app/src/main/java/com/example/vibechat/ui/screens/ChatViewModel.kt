package com.example.vibechat.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechat.data.repository.ChatRepository
import com.example.vibechat.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun sendMessage(userInput: String) {
        val newList = _messages.value + Message(userInput, true)
        _messages.value = newList

        viewModelScope.launch {
            val reply = try {
                repository.sendMessage(userInput)
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
            _messages.value = _messages.value + Message(reply, false)
        }
    }
}