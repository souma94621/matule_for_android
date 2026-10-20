package com.example.network.common

/**
 * Результат вызова сетевого слоя: либо данные, либо ошибка с текстом для показа пользователю.
 */
sealed class ApiResult<out T> {
    /** Успех. [data] — полученные данные. */
    data class Success<T>(val data: T) : ApiResult<T>()

    /** Ошибка. [message] — текст для уведомления, [code] — HTTP-код (если ошибка пришла от сервера). */
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
}