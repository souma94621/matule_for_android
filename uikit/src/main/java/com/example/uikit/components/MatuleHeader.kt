package com.example.uikit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.theme.MatuleTextPrimary

/**
 * Хэдер экрана Matule: заголовок по центру, справа необязательный элемент действия.
 *
 * @param title заголовок экрана
 * @param modifier модификатор для внешнего управления
 * @param action содержимое справа (например, иконка «+»); если null — ничего не показывается
 */
@Composable
fun MatuleHeader(
    title: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            color = MatuleTextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
        if (action != null) {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                action()
            }
        }
    }
}