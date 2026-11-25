package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.LastScreenRepository
import com.example.myapplication.domain.repository.PinRepository
import com.example.myapplication.domain.repository.ReminderScheduler

/** Выход: стирает сессию, PIN-код, напоминания и запомненный экран. */
class LogoutUseCase(
    private val auth: AuthRepository,
    private val pins: PinRepository,
    private val reminders: ReminderScheduler,
    private val lastScreen: LastScreenRepository
) {
    operator fun invoke() {
        auth.logout()
        pins.clear()
        reminders.cancelAll()
        lastScreen.clear()
    }
}