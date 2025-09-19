package com.example.vibechat.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.vibechat.model.Message
import com.example.vibechat.R

val emotionBackgroundColors = mapOf(
    "happy" to Color(0xFFFFFDE7),
    "sad" to Color(0xFFE3F2FD),
    "angry" to Color(0xFFFFEBEE),
    "excited" to Color(0xFFFFF3E0),
    "fearful" to Color(0xFFF3E5F5),
    "neutral" to Color(0xFFF2F2F2)
)

val emotionBubbleColors = mapOf(
    "happy" to Color(0xFFFFF9C4),
    "sad" to Color(0xFFBBDEFB),
    "angry" to Color(0xFFFFCDD2),
    "excited" to Color(0xFFFFCCBC),
    "fearful" to Color(0xFFE1BEE7),
    "neutral" to Color.White
)

@Composable
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel()) {
    val messages by viewModel.messages.collectAsState()
    val emotion by viewModel.currentEmotion.collectAsState()
    val holiday by viewModel.currentHoliday.collectAsState()
    var inputText by remember { mutableStateOf(TextFieldValue("")) }

    val backgroundRes = when (emotion) {
        "happy" -> R.drawable.happy
        "sad" -> R.drawable.sad
        "angry" -> R.drawable.angry
        "excited" -> R.drawable.excited
        "fearful" -> R.drawable.fearful
        else -> R.drawable.neutral
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(emotionBackgroundColors[emotion] ?: Color(0xFFF2F2F2))
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { msg ->
                    MessageBubble(msg, emotion)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE0F7FA),
                        unfocusedContainerColor = Color(0xFFE0F7FA),
                        disabledContainerColor = Color.LightGray,
                        cursorColor = Color.Blue,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
                Button(
                    onClick = {
                        if (inputText.text.isNotBlank()) {
                            viewModel.sendMessage(inputText.text)
                            inputText = TextFieldValue("")
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Send")
                }
            }
        }

        // Lottie animation
        HolidayAnimation(holiday = holiday?.lowercase())
    }
}

@Composable
fun MessageBubble(message: Message, currentEmotion: String) {
    val bubbleColor = if (message.isUser) Color(0xFFDCF8C6)
        else emotionBubbleColors[currentEmotion] ?: Color.White
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        contentAlignment = alignment
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = bubbleColor,
            shadowElevation = 2.dp,
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun HolidayAnimation(holiday: String?) {
    val holidayAnimations = mapOf(
        "christmas" to R.raw.christmas,
        "new year" to R.raw.newyear,
        "lunar new year" to R.raw.lunar_new_year,
        "diwali" to R.raw.diwali,
        "thanksgiving" to R.raw.thanksgiving,
        "halloween" to R.raw.halloween,
        "pride" to R.raw.pride,
        "valentine’s day" to R.raw.valentine
    )

    val resId = holiday?.lowercase()?.let { holidayAnimations[it] }

    if (resId != null) {
        key(resId) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
            val lottieState = animateLottieCompositionAsState(
                composition = composition,
                iterations = 2,
                isPlaying = true
            )

            val visible = lottieState.progress < 0.99f

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = lottieState.progress,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}