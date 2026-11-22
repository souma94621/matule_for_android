package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.validation.isValidEmail

/** Вход пользователя. Правила проверки данных живут здесь, а не в экране. */
class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Resource<User> {
        if (email.isBlank() || password.isBlank()) {
            return Resource.Failure("Заполните e-mail и пароль")
        }
        if (!isValidEmail(email.trim())) {
            return Resource.Failure("Введите e-mail в формате name@domain.ru")
        }
        return repository.login(email.trim(), password)
    }
}