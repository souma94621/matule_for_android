package com.example.myapplication.presentation.pin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.components.MatuleNumpad
import com.example.uikit.components.MatulePinDots
import com.example.uikit.theme.MatuleError
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleTextSecondary

/** Экран PIN-кода: создание или вход. [onForgot] показывает ссылку «Забыли пароль?» (только для входа). */
@Composable
fun PinScreen(
    viewModel: PinViewModel,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    onForgot: (() -> Unit)? = null
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.done) {
        if (state.done) onDone()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = state.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            text = state.subtitle,
            color = MatuleTextSecondary,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        MatulePinDots(total = PinViewModel.PIN_LENGTH, filled = state.entered.length)
        Text(
            text = state.error ?: "",
            color = MatuleError,
            modifier = Modifier
                .height(32.dp)
                .padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        MatuleNumpad(onDigit = viewModel::onDigit, onBackspace = viewModel::onBackspace)
        if (onForgot != null) {
            TextButton(onClick = onForgot) {
                Text("Забыли пароль? Войти заново", color = MatulePrimary)
            }
        }
    }
}