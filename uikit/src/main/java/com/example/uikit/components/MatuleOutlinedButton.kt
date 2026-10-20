package com.example.uikit.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uikit.theme.MatuleBackground
import com.example.uikit.theme.MatuleBorder
import com.example.uikit.theme.MatuleTextPrimary

/**
 * Вторичная кнопка Matule: белая, с серой рамкой (например «Войти с VK»).
 *
 * @param text текст на кнопке
 * @param onClick вызывается при нажатии
 * @param modifier модификатор для внешнего управления размером/отступами
 * @param enabled активна ли кнопка
 * @param icon необязательная иконка слева от текста
 */
@Composable
fun MatuleOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MatuleBorder),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MatuleBackground,
            contentColor = MatuleTextPrimary
        ),
        contentPadding = PaddingValues(vertical = 14.dp)
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = text)
    }
}