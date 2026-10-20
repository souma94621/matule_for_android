package com.example.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleSurfaceMuted
import com.example.uikit.theme.MatuleTextPrimary
import com.example.uikit.theme.MatuleTextSecondary

/**
 * Селект Matule: поле с подписью сверху; по нажатию открывает BottomSheet со списком вариантов.
 *
 * @param label подпись над полем (например «Пол»)
 * @param placeholder серый текст, пока ничего не выбрано
 * @param options список вариантов; смайлики можно писать прямо в тексте («👨 Мужской»)
 * @param selected выбранный вариант; null, если ничего не выбрано
 * @param onSelected вызывается при выборе варианта
 * @param modifier модификатор для внешнего управления размером/отступами
 */
@Composable
fun MatuleSelect(
    label: String,
    placeholder: String,
    options: List<String>,
    selected: String?,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isOpen by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MatuleTextSecondary,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MatuleSurfaceMuted)
                .clickable { isOpen = true }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selected ?: placeholder,
                color = if (selected == null) MatuleTextSecondary else MatuleTextPrimary
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MatuleTextSecondary
            )
        }
    }

    if (isOpen) {
        MatuleBottomSheet(
            onDismiss = { isOpen = false },
            title = label
        ) {
            options.forEach { option ->
                Text(
                    text = option,
                    color = if (option == selected) MatulePrimary else MatuleTextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelected(option)
                            isOpen = false
                        }
                        .padding(vertical = 14.dp)
                )
            }
        }
    }
}