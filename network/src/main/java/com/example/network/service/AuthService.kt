package com.example.network.service

import com.example.network.common.ApiResult
import com.example.network.model.ProfileUpdate
import com.example.network.model.UserDto

/** Авторизация, регистрация и профиль. Приложение работает только с этим интерфейсом. */
interface AuthService {
    /** Вход по e-mail и паролю. Токен сохраняется в сессии автоматически. */
    suspend fun login(email: String, password: String): ApiResult<UserDto>

    /** Создание пользователя и сразу вход. Пол: male или female, дата рождения: yyyy-MM-dd. */
    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        middleName: String,
        lastName: String,
        birthDate: String,
        gender: String
    ): ApiResult<UserDto>

    /** Данные профиля текущего пользователя. */
    suspend fun getProfile(): ApiResult<UserDto>

    /** Изменение профиля текущего пользователя. */
    suspend fun updateProfile(update: ProfileUpdate): ApiResult<UserDto>

    /** Выход: у Pocketbase нет запроса выхода, поэтому просто стираем сессию. */
    fun logout()

    /** true, если в сессии есть токен. */
    fun isLoggedIn(): Boolean
}