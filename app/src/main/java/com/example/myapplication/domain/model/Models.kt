package com.example.myapplication.domain.model

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val birthDate: String,
    val gender: String
)

data class Category(
    val id: String,
    val name: String,
    val targetAudience: String
)

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val categoryId: String,
    val imageUrl: String?,
    val estimatedConsumption: String
)

data class Banner(
    val id: String,
    val title: String,
    val priceLabel: String,
    val imageUrl: String?,
    val productId: String,
    val sortOrder: Int
)

/** Данные для регистрации; birthDate в формате «дд.мм.гггг», gender: male или female. */
data class RegistrationData(
    val email: String,
    val password: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val birthDate: String,
    val gender: String
)