package com.example.myapplication.domain.repository

/** Локальные напоминания. Как они доставляются, домен не знает. */
interface ReminderScheduler {
    /** Напоминание через 3 минуты после последнего открытия проекта (заменяет предыдущее). */
    fun scheduleProjectInactivity(projectTitle: String)

    /** Напоминание «давно не заходили» через 1 минуту (ставится, когда приложение уходит в фон). */
    fun scheduleAppInactivity()

    /** Отмена напоминания о приложении (когда пользователь вернулся). */
    fun cancelAppInactivity()

    /** Отмена всех напоминаний (при выходе из аккаунта). */
    fun cancelAll()
}