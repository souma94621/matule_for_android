package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.PinRepository

/** Выход: стирает сессию и PIN-код. */
class LogoutUseCase(
    private val auth: AuthRepository,
    private val pins: PinRepository
) {
    operator fun invoke() {
        auth.logout()
        pins.clear()
    }
}