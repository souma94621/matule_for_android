package com.example.myapplication.domain.repository

/** Хранение PIN-кода для входа в приложение. */
interface PinRepository {
    fun hasPin(): Boolean
    fun savePin(pin: String)
    fun checkPin(pin: String): Boolean
    fun clear()
}