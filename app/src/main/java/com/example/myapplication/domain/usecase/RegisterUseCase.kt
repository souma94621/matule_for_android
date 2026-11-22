package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.RegistrationData
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.validation.birthDateToIso
import com.example.myapplication.domain.validation.checkPassword
import com.example.myapplication.domain.validation.isValidEmail

/** Регистрация: проверяет все поля и создаёт пользователя. */
class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(data: RegistrationData): Resource<User> {
        if (listOf(data.firstName, data.middleName, data.lastName).any { it.isBlank() }) {
            return Resource.Failure("Заполните все поля профиля")
        }
        if (!isValidEmail(data.email)) {
            return Resource.Failure("Введите e-mail в формате name@domain.ru")
        }
        val isoDate = birthDateToIso(data.birthDate)
            ?: return Resource.Failure("Укажите дату рождения в формате дд.мм.гггг")
        if (!checkPassword(data.password).isStrong) {
            return Resource.Failure("Пароль не соответствует требованиям")
        }
        return repository.register(data.copy(birthDate = isoDate))
    }
}