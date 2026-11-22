package com.example.myapplication.domain.validation

/** Результат проверки пароля по каждому требованию. */
data class PasswordCheck(
    val hasMinLength: Boolean,
    val hasUpper: Boolean,
    val hasLower: Boolean,
    val hasDigit: Boolean,
    val hasSpecial: Boolean
) {
    val isStrong: Boolean get() = hasMinLength && hasUpper && hasLower && hasDigit && hasSpecial
}

/** Требования задания: от 8 символов, заглавные и строчные буквы, цифры, спецсимволы (пробел тоже считается). */
fun checkPassword(password: String) = PasswordCheck(
    hasMinLength = password.length >= 8,
    hasUpper = password.any { it.isUpperCase() },
    hasLower = password.any { it.isLowerCase() },
    hasDigit = password.any { it.isDigit() },
    hasSpecial = password.any { !it.isLetterOrDigit() }
)