package com.example.myapplication.domain.validation

private val EMAIL_REGEX = Regex("^[a-z0-9]+@[a-z0-9]+\\.ru$")

/** Проверка по заданию: name@domenname.ru, имя и домен только из строчных букв и цифр. */
fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)