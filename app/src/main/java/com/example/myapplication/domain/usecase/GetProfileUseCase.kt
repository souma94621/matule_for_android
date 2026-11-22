package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.AuthRepository

/** Получить профиль текущего пользователя. */
class GetProfileUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Resource<User> = repository.getProfile()
}