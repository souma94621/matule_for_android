package com.example.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleSurfaceMuted
import com.example.uikit.theme.MatuleTextPrimary

/**
 * Ряд кружков-индикаторов PIN-кода.
 *
 * @param total сколько всего цифр в коде
 * @param filled сколько уже введено (закрашенные кружки)
 */
@Composable
fun MatulePinDots(total: Int, filled: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        repeat(total) { index ->
            val shape = Modifier.size(16.dp).clip(CircleShape)
            Box(
                modifier = if (index < filled) {
                    shape.background(MatulePrimary)
                } else {
                    shape.border(1.5.dp, MatulePrimary, CircleShape)
                }
            )
        }
    }
}

/**
 * Цифровая клавиатура для PIN-кода: цифры 0-9 и стирание.
 *
 * @param onDigit вызывается с нажатой цифрой
 * @param onBackspace вызывается при нажатии на стирание
 */
@Composable
fun MatuleNumpad(
    onDigit: (Int) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9)).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                row.forEach { digit -> NumKey(digit.toString()) { onDigit(digit) } }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Spacer(modifier = Modifier.size(72.dp))
            NumKey("0") { onDigit(0) }
            NumKey("⌫", onBackspace)
        }
    }
}

@Composable
private fun NumKey(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MatuleSurfaceMuted)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 28.sp, color = MatuleTextPrimary)
    }
}