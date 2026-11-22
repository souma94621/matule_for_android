package com.example.myapplication.presentation.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleSelect
import com.example.uikit.components.MatuleTextField
import com.example.uikit.theme.MatuleTextSecondary

/** Экран «Создание профиля»: все поля обязательны. */
@Composable
fun RegistrationProfileScreen(
    viewModel: RegistrationViewModel,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Создание Профиля", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Без профиля вы не сможете создавать проекты.\nВ профиле будут храниться результаты проектов и ваши описания.",
            color = MatuleTextSecondary
        )
        MatuleTextField(
            value = state.firstName,
            onValueChange = { v -> viewModel.update { copy(firstName = v) } },
            label = "Имя", placeholder = "Имя"
        )
        MatuleTextField(
            value = state.middleName,
            onValueChange = { v -> viewModel.update { copy(middleName = v) } },
            label = "Отчество", placeholder = "Отчество"
        )
        MatuleTextField(
            value = state.lastName,
            onValueChange = { v -> viewModel.update { copy(lastName = v) } },
            label = "Фамилия", placeholder = "Фамилия"
        )
        MatuleTextField(
            value = state.birthDate,
            onValueChange = { v -> viewModel.update { copy(birthDate = v) } },
            label = "Дата рождения", placeholder = "дд.мм.гггг",
            errorText = state.birthDateError
        )
        MatuleSelect(
            label = "Пол",
            placeholder = "Пол",
            options = GENDER_OPTIONS,
            selected = state.gender,
            onSelected = { v -> viewModel.update { copy(gender = v) } }
        )
        MatuleTextField(
            value = state.email,
            onValueChange = { v -> viewModel.update { copy(email = v) } },
            label = "Почта", placeholder = "Почта",
            errorText = state.emailError,
            keyboardType = KeyboardType.Email
        )
        MatuleButton(text = "Далее", onClick = onNext, enabled = state.canContinueProfile)
    }
}