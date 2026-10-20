package com.example.uikit.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uikit.theme.MatuleBorder
import com.example.uikit.theme.MatuleSurfaceMuted
import com.example.uikit.theme.MatuleTextSecondary

/**
 * Поле поиска Matule: серый фон, иконка лупы слева, одна строка.
 *
 * @param value текущий текст запроса
 * @param onValueChange вызывается при каждом изменении текста
 * @param placeholder серый текст-подсказка, пока поле пустое (например «Искать описания»)
 * @param modifier модификатор для внешнего управления размером/отступами
 */
@Composable
fun MatuleSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = MatuleTextSecondary) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MatuleTextSecondary
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MatuleSurfaceMuted,
            unfocusedContainerColor = MatuleSurfaceMuted,
            focusedBorderColor = MatuleBorder,
            unfocusedBorderColor = MatuleBorder
        )
    )
}