package com.example.myapplication.domain.repository

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.RegistrationData
import com.example.myapplication.domain.model.User

/** Что приложению нужно от авторизации. Как это сделано, домен не знает. */
interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun register(data: RegistrationData): Resource<User>
    suspend fun getProfile(): Resource<User>
    fun isLoggedIn(): Boolean
    fun logout()
}