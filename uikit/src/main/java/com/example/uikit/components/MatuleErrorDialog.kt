package com.example.uikit.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.uikit.theme.MatuleBackground
import com.example.uikit.theme.MatulePrimary

/**
 * Окно с текстом ошибки; закрывается только по кнопке.
 *
 * @param message текст ошибки
 * @param onDismiss вызывается при нажатии на кнопку «Закрыть» (или тапе вне окна)
 */
@Composable
fun MatuleErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MatuleBackground,
        title = { Text("Ошибка") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть", color = MatulePrimary)
            }
        }
    )
}