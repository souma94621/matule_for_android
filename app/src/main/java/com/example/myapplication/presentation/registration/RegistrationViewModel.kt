package com.example.myapplication.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.RegistrationData
import com.example.myapplication.domain.usecase.RegisterUseCase
import com.example.myapplication.domain.validation.PasswordCheck
import com.example.myapplication.domain.validation.birthDateToIso
import com.example.myapplication.domain.validation.checkPassword
import com.example.myapplication.domain.validation.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Варианты пола в селекте (со смайликами, как требует задание). */
val GENDER_OPTIONS = listOf("👨 Мужской", "👩 Женский")

/** Данные обоих экранов регистрации (профиль + пароль) в одном состоянии. */
data class RegistrationUiState(
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val birthDate: String = "",
    val gender: String? = null,
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistered: Boolean = false
) {
    val emailError: String?
        get() = if (email.isNotEmpty() && !isValidEmail(email)) "Формат e-mail: name@domain.ru" else null

    val birthDateError: String?
        get() = if (birthDate.isNotEmpty() && birthDateToIso(birthDate) == null) "Дата в формате дд.мм.гггг" else null

    /** «Далее» на экране профиля: все поля обязательны. */
    val canContinueProfile: Boolean
        get() = firstName.isNotBlank() && middleName.isNotBlank() && lastName.isNotBlank() &&
                gender != null && isValidEmail(email) && birthDateToIso(birthDate) != null

    val passwordCheck: PasswordCheck get() = checkPassword(password)

    val repeatError: String?
        get() = if (repeatPassword.isNotEmpty() && repeatPassword != password) "Пароли не совпадают" else null

    val canSave: Boolean
        get() = passwordCheck.isStrong && password == repeatPassword && !isLoading
}

class RegistrationViewModel(private val register: RegisterUseCase) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationUiState())
    val state: StateFlow<RegistrationUiState> = _state.asStateFlow()

    /** Меняет состояние: viewModel.update { copy(firstName = "...") }. */
    fun update(reducer: RegistrationUiState.() -> RegistrationUiState) {
        _state.update { it.reducer() }
    }

    fun onSave() {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val data = RegistrationData(
                email = s.email.trim(),
                password = s.password,
                firstName = s.firstName.trim(),
                middleName = s.middleName.trim(),
                lastName = s.lastName.trim(),
                birthDate = s.birthDate,
                gender = if (s.gender == GENDER_OPTIONS[1]) "female" else "male"
            )
            when (val result = register(data)) {
                is Resource.Success -> _state.update { it.copy(isLoading = false, isRegistered = true) }
                is Resource.Failure -> _state.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }

    fun onErrorDismissed() {
        _state.update { it.copy(errorMessage = null) }
    }
}