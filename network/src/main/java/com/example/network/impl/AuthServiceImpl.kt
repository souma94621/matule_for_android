package com.example.network.impl

import com.example.network.common.ApiResult
import com.example.network.common.SessionStorage
import com.example.network.common.safeCall
import com.example.network.model.ProfileUpdate
import com.example.network.model.UserDto
import com.example.network.service.AuthService

/** Реализация [AuthService] поверх Pocketbase. */
internal class AuthServiceImpl(
    private val api: PocketBaseApi,
    private val session: SessionStorage
) : AuthService {

    override suspend fun login(email: String, password: String): ApiResult<UserDto> = safeCall {
        val response = api.login(LoginRequest(email, password))
        session.token = response.token
        session.userId = response.record.id
        response.record
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        middleName: String,
        lastName: String,
        birthDate: String,
        gender: String
    ): ApiResult<UserDto> = safeCall {
        api.createUser(
            RegisterRequest(email, password, password, firstName, middleName, lastName, birthDate, gender)
        )
        val response = api.login(LoginRequest(email, password))
        session.token = response.token
        session.userId = response.record.id
        response.record
    }

    override suspend fun getProfile(): ApiResult<UserDto> = safeCall {
        val id = session.userId ?: error("Пользователь не авторизован")
        api.getUser(id)
    }

    override suspend fun updateProfile(update: ProfileUpdate): ApiResult<UserDto> = safeCall {
        val id = session.userId ?: error("Пользователь не авторизован")
        api.updateUser(
            id,
            UpdateProfileRequest(
                update.firstName, update.middleName, update.lastName, update.birthDate, update.gender
            )
        )
    }

    override fun logout() = session.clear()

    override fun isLoggedIn(): Boolean = session.token != null
}