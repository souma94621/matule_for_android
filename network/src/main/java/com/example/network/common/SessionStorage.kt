package com.example.network.common

/**
 * Хранилище данных сессии (токен и id пользователя).
 * Сетевой слой знает только этот интерфейс, а где данные хранятся физически
 * (память, DataStore) решает приложение. Так соблюдается принцип инверсии зависимостей.
 */
interface SessionStorage {
    /** Токен авторизации Pocketbase; null, если пользователь не вошёл. */
    var token: String?

    /** Id текущего пользователя; null, если пользователь не вошёл. */
    var userId: String?

    /** Стирает данные сессии (выход из аккаунта). */
    fun clear() {
        token = null
        userId = null
    }
}

/** Простая реализация, хранящая сессию в памяти (пропадает при перезапуске приложения). */
class InMemorySessionStorage : SessionStorage {
    override var token: String? = null
    override var userId: String? = null
}