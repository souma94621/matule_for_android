package com.example.myapplication.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.validation.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Состояние экрана «Вход и регистрация». */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
) {
    /** Кнопка «Далее» активна, когда оба поля заполнены и нет загрузки. */
    val canSubmit: Boolean get() = email.isNotEmpty() && password.isNotEmpty() && !isLoading
}

/** Логика экрана входа. */
class LoginViewModel(private val login: LoginUseCase) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onEmailChange(value: String) {
        _state.update {
            it.copy(
                email = value,
                emailError = if (value.isNotEmpty() && !isValidEmail(value)) {
                    "Формат e-mail: name@domain.ru (строчные буквы и цифры)"
                } else null
            )
        }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value) }
    }

    fun onSubmit() {
        val s = _state.value
        if (!isValidEmail(s.email)) {
            _state.update { it.copy(emailError = "Формат e-mail: name@domain.ru (строчные буквы и цифры)") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = login(s.email, s.password)) {
                is Resource.Success -> _state.update { it.copy(isLoading = false, isLoggedIn = true) }
                is Resource.Failure -> _state.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }

    /** Закрытие окна ошибки. */
    fun onErrorDismissed() {
        _state.update { it.copy(errorMessage = null) }
    }
}