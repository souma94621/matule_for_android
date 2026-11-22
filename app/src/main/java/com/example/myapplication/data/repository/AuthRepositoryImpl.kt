package com.example.myapplication.data.repository

import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.data.mapper.toResource
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.RegistrationData
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.AuthRepository
import com.example.network.service.AuthService

/** Реализация [AuthRepository] поверх сетевого интерфейса [AuthService]. */
class AuthRepositoryImpl(private val service: AuthService) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<User> =
        service.login(email, password).toResource { it.toDomain() }

    override suspend fun register(data: RegistrationData): Resource<User> =
        service.register(
            email = data.email,
            password = data.password,
            firstName = data.firstName,
            middleName = data.middleName,
            lastName = data.lastName,
            birthDate = data.birthDate,
            gender = data.gender
        ).toResource { it.toDomain() }

    override suspend fun getProfile(): Resource<User> =
        service.getProfile().toResource { it.toDomain() }

    override fun isLoggedIn(): Boolean = service.isLoggedIn()

    override fun logout() = service.logout()
}