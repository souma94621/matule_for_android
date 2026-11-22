package com.example.myapplication.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleErrorDialog
import com.example.uikit.components.MatuleOutlinedButton
import com.example.uikit.components.MatuleTextField
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleTextSecondary

/** Экран «Вход и регистрация». */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoggedIn: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onLoggedIn()
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "✋ Добро пожаловать!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Войдите, чтобы пользоваться функциями приложения")
        Spacer(modifier = Modifier.height(16.dp))

        MatuleTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = "Вход по E-mail",
            placeholder = "example@mail.ru",
            errorText = state.emailError,
            keyboardType = KeyboardType.Email
        )
        MatuleTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = "Пароль",
            placeholder = "Введите пароль",
            isPassword = true
        )
        MatuleButton(
            text = if (state.isLoading) "Вход..." else "Далее",
            onClick = viewModel::onSubmit,
            enabled = state.canSubmit
        )
        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Зарегистрироваться", color = MatulePrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Или войдите с помощью",
            color = MatuleTextSecondary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        MatuleOutlinedButton(text = "Войти с VK", onClick = {})
        MatuleOutlinedButton(text = "Войти с Yandex", onClick = {})
    }

    state.errorMessage?.let { message ->
        MatuleErrorDialog(message = message, onDismiss = viewModel::onErrorDismissed)
    }
}