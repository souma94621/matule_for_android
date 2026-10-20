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
 * Карточка товара Matule: название, категория, цена и кнопка действия.
 *
 * @param title название товара
 * @param category категория серым цветом под названием
 * @param price цена, уже отформатированная строкой (например «300 ₽»)
 * @param onAddClick вызывается при нажатии на кнопку действия
 * @param modifier модификатор для внешнего управления размером/отступами
 * @param buttonText текст кнопки (по умолчанию «Добавить»)
 */
@Composable
fun MatuleCard(
    title: String,
    category: String,
    price: String,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = "Добавить"
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
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(text = category, color = MatuleTextSecondary)
                    Text(
                        text = price,
                        color = MatuleTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                MatuleButton(
                    text = buttonText,
                    onClick = onAddClick,
                    modifier = Modifier
                        .width(120.dp)
                        .height(44.dp)
                )
            }
        }
    }
}