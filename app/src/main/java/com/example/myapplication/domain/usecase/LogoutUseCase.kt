package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.PinRepository
import com.example.myapplication.domain.repository.ReminderScheduler

/** Выход: стирает сессию, PIN-код и запланированные напоминания. */
class LogoutUseCase(
    private val auth: AuthRepository,
    private val pins: PinRepository,
    private val reminders: ReminderScheduler
) {
    operator fun invoke() {
        auth.logout()
        pins.clear()
        reminders.cancelAll()
    }
}