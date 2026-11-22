package com.example.myapplication.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/** Заставка: градиент и название, через 1,5 секунды переходит дальше. Цвета градиента подобраны на глаз по макету. */
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onFinished()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF70B8E4), Color(0xFF2756F4), Color(0xFF70B8E4))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Matule", color = Color.White, fontSize = 40.sp)
    }
}