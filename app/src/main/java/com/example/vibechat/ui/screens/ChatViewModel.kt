package com.example.vibechat.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechat.data.repository.ChatRepository
import com.example.vibechat.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _currentEmotion = MutableStateFlow("neutral")
    val currentEmotion: StateFlow<String> = _currentEmotion

    private val _currentHoliday = MutableStateFlow<String?>(null)
    val currentHoliday: StateFlow<String?> = _currentHoliday

    fun sendMessage(userInput: String) {
        val newList = _messages.value + Message(userInput, true)
        _messages.value = newList


        viewModelScope.launch {
            val reply = try {
                repository.sendMessage(userInput) // 返回 EmotionResponse
            } catch (e: Exception) {
                null
            }

            if (reply != null) {
                _currentEmotion.value = reply.emotion
                _currentHoliday.value = reply.holiday
                _messages.value = _messages.value + Message(reply.message, isUser = false)
                Log.d("test>>>ChatViewModel", "AI Reply: ${reply.message}, Emotion: ${reply.emotion}, Holiday: ${reply.holiday}")

            } else {
                _messages.value = _messages.value + Message("Error: no response", isUser = false)
                _currentEmotion.value = "neutral"
                _currentHoliday.value = null
            }
        }
    }
}