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

data class CartItem(val id: String, val product: Product, val quantity: Int)

data class Cart(val items: List<CartItem>, val total: Double)

/** Фото в виде байтов (уже сжатое JPEG). */
class PhotoData(
    val bytes: ByteArray,
    val fileName: String = "photo.jpg",
    val mimeType: String = "image/jpeg"
)

data class Project(
    val id: String,
    val title: String,
    val type: String,
    val startDate: String,
    val endDate: String,
    val recipient: String,
    val descriptionSource: String,
    val categoryId: String,
    val photoUrl: String?,
    val createdAt: String
)

/** Данные нового проекта (без фото); даты на входе в формате «дд.мм.гггг». */
data class NewProjectData(
    val type: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val recipient: String,
    val descriptionSource: String,
    val categoryId: String
)

/** Данные главной страницы; fromCache = true, если показаны сохранённые данные из-за отсутствия сети. */
data class HomeData(
    val banners: List<Banner>,
    val categories: List<Category>,
    val products: List<Product>,
    val fromCache: Boolean = false
)

/** Незавершённая регистрация (пароли сюда не попадают). */
data class RegistrationDraft(
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val birthDate: String = "",
    val gender: String? = null,
    val email: String = ""
)