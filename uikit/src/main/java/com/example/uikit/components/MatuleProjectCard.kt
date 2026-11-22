package com.example.uikit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.theme.MatuleBackground
import com.example.uikit.theme.MatuleTextPrimary
import com.example.uikit.theme.MatuleTextSecondary

/**
 * Карточка проекта: название, подпись серым (например «Прошло 2 дня») и кнопка «Открыть».
 *
 * @param title название проекта
 * @param subtitle серая подпись слева внизу
 * @param onOpen вызывается при нажатии на кнопку
 * @param modifier модификатор для внешнего управления
 */
@Composable
fun MatuleProjectCard(
    title: String,
    subtitle: String,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MatuleBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = MatuleTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = subtitle, color = MatuleTextSecondary)
                MatuleButton(
                    text = "Открыть",
                    onClick = onOpen,
                    modifier = Modifier
                        .width(120.dp)
                        .height(44.dp)
                )
            }
        }
    }
}