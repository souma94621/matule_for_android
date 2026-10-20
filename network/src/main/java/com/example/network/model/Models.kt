package com.example.network.model

import com.google.gson.annotations.SerializedName

/** Ответ Pocketbase со списком записей. */
data class ListResponse<T>(
    val page: Int = 1,
    val perPage: Int = 0,
    val totalItems: Int = 0,
    val items: List<T> = emptyList()
)

/** Пользователь. gender: male или female. */
data class UserDto(
    val id: String = "",
    val email: String = "",
    @SerializedName("first_name") val firstName: String = "",
    @SerializedName("middle_name") val middleName: String = "",
    @SerializedName("last_name") val lastName: String = "",
    @SerializedName("birth_date") val birthDate: String = "",
    val gender: String = ""
)

/** Ответ на вход: токен и данные пользователя. */
data class AuthResponse(
    val token: String = "",
    val record: UserDto = UserDto()
)

/** Баннер блока «Акции и новости». */
data class BannerDto(
    val id: String = "",
    val collectionId: String = "",
    val title: String = "",
    @SerializedName("price_label") val priceLabel: String = "",
    val image: String = "",
    val product: String = "",
    @SerializedName("sort_order") val sortOrder: Int = 0
)

/** Категория товаров. target_audience: all, women или men. */
data class CategoryDto(
    val id: String = "",
    val name: String = "",
    @SerializedName("target_audience") val targetAudience: String = ""
)

/** Товар (описание). */
data class ProductDto(
    val id: String = "",
    val collectionId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val image: String = "",
    @SerializedName("estimated_consumption") val estimatedConsumption: String = ""
)

/** Заказ; пока status = cart, он является корзиной. */
data class OrderDto(
    val id: String = "",
    val user: String = "",
    val status: String = "",
    @SerializedName("total_price") val totalPrice: Double = 0.0
)

/** Позиция заказа (строка корзины). */
data class OrderItemDto(
    val id: String = "",
    val order: String = "",
    val product: String = "",
    val quantity: Int = 1,
    @SerializedName("price_at_purchase") val priceAtPurchase: Double = 0.0,
    val expand: OrderItemExpand? = null
)

/** Развёрнутые связи позиции заказа. */
data class OrderItemExpand(
    val product: ProductDto? = null
)

/** Проект пользователя. */
data class ProjectDto(
    val id: String = "",
    val collectionId: String = "",
    val user: String = "",
    val type: String = "",
    val title: String = "",
    @SerializedName("start_date") val startDate: String = "",
    @SerializedName("end_date") val endDate: String = "",
    val recipient: String = "",
    @SerializedName("description_source") val descriptionSource: String = "",
    val category: String = "",
    val photo: String = "",
    val created: String = ""
)

/** Позиция корзины с полным товаром. */
data class CartItem(
    val id: String,
    val product: ProductDto,
    val quantity: Int
)

/** Корзина пользователя. [total] — итоговая сумма. */
data class Cart(
    val orderId: String,
    val items: List<CartItem>,
    val total: Double
)

/** Изменение профиля; передаются только те поля, которые нужно поменять (остальные null). */
data class ProfileUpdate(
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val birthDate: String? = null,
    val gender: String? = null
)

/** Файл фото в виде байтов (приложение читает его из галереи/камеры и передаёт сюда). */
class PhotoFile(
    val bytes: ByteArray,
    val fileName: String = "photo.jpg",
    val mimeType: String = "image/jpeg"
)

/** Данные для создания проекта. Даты в формате yyyy-MM-dd. */
data class NewProject(
    val type: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val recipient: String,
    val descriptionSource: String,
    val categoryId: String,
    val photo: PhotoFile
)