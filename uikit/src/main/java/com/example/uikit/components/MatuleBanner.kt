package com.example.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Набор градиентов для баннеров (по кругу): бирюзовый и голубой, как в макете. */
val MatuleBannerGradients: List<List<Color>> = listOf(
    listOf(Color(0xFF8EDDE8), Color(0xFF98E8D8)),
    listOf(Color(0xFF7DB5FF), Color(0xFF9CC4FF))
)

/**
 * Баннер блока «Акции и новости»: заголовок сверху, цена снизу, фон градиентом.
 *
 * @param title заголовок белым цветом
 * @param priceLabel подпись внизу (например «4000 ₽»)
 * @param gradient два цвета градиента фона
 * @param modifier модификатор для внешнего управления
 * @param onClick необязательное действие при нажатии
 */
@Composable
fun MatuleBanner(
    title: String,
    priceLabel: String,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val clickable = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Column(
        modifier = modifier
            .width(270.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(gradient))
            .then(clickable)
            .padding(20.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
        Text(
            text = priceLabel,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}