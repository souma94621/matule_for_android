package com.example.myapplication.presentation.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleErrorDialog
import com.example.uikit.components.MatuleTextField
import com.example.uikit.theme.MatuleError
import com.example.uikit.theme.MatuleTextSecondary

/** Экран «Создание пароля» профиля: показ пароля и проверка надёжности. */
@Composable
fun RegistrationPasswordScreen(
    viewModel: RegistrationViewModel,
    onRegistered: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val check = state.passwordCheck

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) onRegistered()
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "✋ Создание пароля", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Введите новый пароль")
        MatuleTextField(
            value = state.password,
            onValueChange = { v -> viewModel.update { copy(password = v) } },
            label = "Новый Пароль", placeholder = "Введите пароль",
            isPassword = true
        )
        MatuleTextField(
            value = state.repeatPassword,
            onValueChange = { v -> viewModel.update { copy(repeatPassword = v) } },
            label = "Повторите пароль", placeholder = "Повторите пароль",
            isPassword = true,
            errorText = state.repeatError
        )
        Requirement("Не менее 8 символов", check.hasMinLength)
        Requirement("Заглавные буквы", check.hasUpper)
        Requirement("Строчные буквы", check.hasLower)
        Requirement("Цифры", check.hasDigit)
        Requirement("Специальные символы или пробел", check.hasSpecial)
        MatuleButton(
            text = if (state.isLoading) "Сохранение..." else "Сохранить",
            onClick = viewModel::onSave,
            enabled = state.canSave
        )
    }

    state.errorMessage?.let { message ->
        MatuleErrorDialog(message = message, onDismiss = viewModel::onErrorDismissed)
    }
}

@Composable
private fun Requirement(text: String, done: Boolean) {
    Text(
        text = (if (done) "✓ " else "✗ ") + text,
        color = if (done) MatuleTextSecondary else MatuleError
    )
}