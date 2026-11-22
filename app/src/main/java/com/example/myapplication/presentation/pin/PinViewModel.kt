package com.example.myapplication.presentation.pin

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.repository.PinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** CREATE: создание кода с повтором; ENTER: ввод существующего. */
enum class PinMode { CREATE, ENTER }

data class PinUiState(
    val title: String,
    val subtitle: String,
    val entered: String = "",
    val error: String? = null,
    val done: Boolean = false
)

class PinViewModel(
    private val mode: PinMode,
    private val pins: PinRepository
) : ViewModel() {

    private var firstPin: String? = null

    private val _state = MutableStateFlow(initial())
    val state: StateFlow<PinUiState> = _state.asStateFlow()

    private fun initial() = if (mode == PinMode.CREATE) {
        PinUiState("Создайте пароль", "Для защиты ваших персональных данных")
    } else {
        PinUiState("Вход", "Введите пароль")
    }

    fun onDigit(digit: Int) {
        val current = _state.value
        if (current.done || current.entered.length >= PIN_LENGTH) return
        val entered = current.entered + digit
        _state.value = current.copy(entered = entered, error = null)
        if (entered.length == PIN_LENGTH) onComplete(entered)
    }

    fun onBackspace() {
        _state.update { it.copy(entered = it.entered.dropLast(1), error = null) }
    }

    private fun onComplete(pin: String) {
        when (mode) {
            PinMode.ENTER -> {
                if (pins.checkPin(pin)) {
                    _state.update { it.copy(done = true) }
                } else {
                    _state.value = initial().copy(error = "Неверный пароль")
                }
            }
            PinMode.CREATE -> {
                val first = firstPin
                when {
                    first == null -> {
                        firstPin = pin
                        _state.value = PinUiState("Повторите пароль", "Введите пароль ещё раз")
                    }
                    first == pin -> {
                        pins.savePin(pin)
                        _state.update { it.copy(done = true) }
                    }
                    else -> {
                        firstPin = null
                        _state.value = initial().copy(error = "Пароли не совпадают")
                    }
                }
            }
        }
    }

    companion object {
        const val PIN_LENGTH = 4
    }
}