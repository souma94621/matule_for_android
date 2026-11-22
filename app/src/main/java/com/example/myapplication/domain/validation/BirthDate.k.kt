package com.example.myapplication.domain.validation

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

private val INPUT_FORMAT = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT)

/** Переводит дату «дд.мм.гггг» в формат сервера «гггг-мм-дд». Возвращает null, если дата некорректна или в будущем. */
fun birthDateToIso(input: String): String? = try {
    val date = LocalDate.parse(input.trim(), INPUT_FORMAT)
    if (date.isAfter(LocalDate.now())) null else date.toString()
} catch (e: DateTimeParseException) {
    null
}