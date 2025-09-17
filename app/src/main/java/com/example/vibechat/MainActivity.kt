package com.example.vibechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.vibechat.data.remote.RetrofitClient
import com.example.vibechat.data.repository.ChatRepository
import com.example.vibechat.ui.screens.ChatScreen
import com.example.vibechat.ui.screens.ChatViewModel
import com.example.vibechat.ui.theme.VibeChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val baseUrl = ""
        val apiKey = ""
        val deployment = ""
        val apiVersion = ""

        val api = RetrofitClient.create(baseUrl, apiKey)
        val repository = ChatRepository(api, deployment, apiVersion)
        val chatViewModel = ChatViewModel(repository)



        setContent {
            VibeChatTheme {
                ChatScreen(chatViewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VibeChatTheme {
        Greeting("Android")
    }
}