package com.example.myapplication.domain.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import java.time.temporal.ChronoUnit

private val INPUT_FORMAT = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT)

/** Разбирает дату «дд.мм.гггг»; null, если дата некорректна. */
fun parseDate(input: String): LocalDate? = try {
    LocalDate.parse(input.trim(), INPUT_FORMAT)
} catch (e: DateTimeParseException) {
    null
}

/** Переводит дату сервера («гггг-мм-дд ...») в «дд.мм.гггг». */
fun formatIsoDate(iso: String): String = try {
    LocalDate.parse(iso.take(10)).format(INPUT_FORMAT)
} catch (e: DateTimeParseException) {
    iso
}

/** Подпись вида «Прошло 2 дня» по дате создания. */
fun formatDaysAgo(created: String): String {
    val date = try {
        LocalDate.parse(created.take(10))
    } catch (e: DateTimeParseException) {
        return ""
    }
    val days = ChronoUnit.DAYS.between(date, LocalDate.now()).toInt().coerceAtLeast(0)
    if (days == 0) return "Сегодня"
    val verb = if (days % 10 == 1 && days % 100 != 11) "Прошёл" else "Прошло"
    val word = when {
        days % 100 in 11..14 -> "дней"
        days % 10 == 1 -> "день"
        days % 10 in 2..4 -> "дня"
        else -> "дней"
    }
    return "$verb $days $word"
}