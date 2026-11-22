package com.example.myapplication.domain.common

/** Результат операции в терминах домена: данные или текст ошибки для показа пользователю. */
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Failure(val message: String) : Resource<Nothing>()
}