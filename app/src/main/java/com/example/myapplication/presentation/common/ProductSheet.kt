package com.example.myapplication.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.example.myapplication.domain.model.Product
import com.example.uikit.components.MatuleBottomSheet
import com.example.uikit.components.MatuleButton
import com.example.uikit.theme.MatuleTextSecondary

/** Шторка с описанием товара и кнопкой «Добавить / Убрать из корзины». */
@Composable
fun ProductSheet(
    product: Product,
    isAdded: Boolean,
    onToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    // Поле-редактор Pocketbase отдаёт HTML, поэтому убираем теги
    val description = remember(product.id) {
        HtmlCompat.fromHtml(product.description, HtmlCompat.FROM_HTML_MODE_COMPACT).toString().trim()
    }
    MatuleBottomSheet(onDismiss = onDismiss, title = product.name) {
        Text(text = "Описание", color = MatuleTextSecondary)
        Text(text = description, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
        if (product.estimatedConsumption.isNotBlank()) {
            Text(text = "Примерный расход:", color = MatuleTextSecondary)
            Text(text = product.estimatedConsumption, modifier = Modifier.padding(bottom = 16.dp))
        }
        MatuleButton(
            text = if (isAdded) "Убрать из корзины" else "Добавить за ${product.price.toInt()} ₽",
            onClick = {
                onToggle()
                onDismiss()
            }
        )
    }
}