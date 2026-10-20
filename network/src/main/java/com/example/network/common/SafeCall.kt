package com.example.network.common

import com.google.gson.JsonParser
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Выполняет сетевой вызов и превращает любое исключение в [ApiResult.Error]
 * с понятным текстом. Единое место обработки ошибок для всех сервисов (DRY).
 *
 * @param block сетевой вызов
 */
internal suspend fun <T> safeCall(block: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        ApiResult.Error(parseHttpError(e), e.code())
    } catch (e: IOException) {
        ApiResult.Error("Нет подключения к интернету или сервер недоступен")
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Неизвестная ошибка")
    }
}

/** Достаёт текст ошибки из JSON-ответа Pocketbase (поле message и ошибки по полям из data). */
private fun parseHttpError(e: HttpException): String {
    val fallback = "Ошибка сервера (${e.code()})"
    val body = try {
        e.response()?.errorBody()?.string()
    } catch (ex: Exception) {
        null
    }
    if (body.isNullOrBlank()) return fallback

    return try {
        val json = JsonParser.parseString(body).asJsonObject
        val general = json.get("message")?.asString ?: fallback
        val details = json.getAsJsonObject("data")?.entrySet()?.mapNotNull { entry ->
            val msg = entry.value.asJsonObject.get("message")?.asString
            if (msg != null) "${entry.key}: $msg" else null
        }.orEmpty()
        if (details.isEmpty()) general else general + "\n" + details.joinToString("\n")
    } catch (ex: Exception) {
        fallback
    }
}

/** Экранирует значение для подстановки в строку фильтра Pocketbase (защита от кавычек в тексте поиска). */
internal fun escapeFilter(value: String): String =
    value.replace("\\", "\\\\").replace("'", "\\'")