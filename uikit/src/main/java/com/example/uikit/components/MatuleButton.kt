package com.example.uikit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uikit.theme.MatulePrimary

/**
 * Основная кнопка Matule (заполненная, на всю ширину).
 *
 * @param text текст на кнопке
 * @param onClick вызывается при нажатии
 * @param modifier модификатор для внешнего управления размером/отступами
 * @param enabled активна ли кнопка — влияет на цвет (насыщенный/бледный) и кликабельность
 */
@Composable
fun MatuleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MatulePrimary,
            disabledContainerColor = MatulePrimary.copy(alpha = 0.4f)
        ),
        contentPadding = PaddingValues(vertical = 14.dp)
    ) {
        Text(text = text)
    }
}