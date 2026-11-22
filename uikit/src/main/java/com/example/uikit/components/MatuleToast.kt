package com.example.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uikit.theme.MatulePrimary

/**
 * Всплывающее уведомление об успехе. Показом и скрытием управляет вызывающий код.
 *
 * @param message текст уведомления
 */
@Composable
fun MatuleToast(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MatulePrimary)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(text = message, color = Color.White)
    }
}