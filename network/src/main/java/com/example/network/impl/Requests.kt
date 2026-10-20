package com.example.network.impl

import com.google.gson.annotations.SerializedName

internal data class LoginRequest(val identity: String, val password: String)

internal data class RegisterRequest(
    val email: String,
    val password: String,
    val passwordConfirm: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("middle_name") val middleName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("birth_date") val birthDate: String,
    val gender: String
)

internal data class UpdateProfileRequest(
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("middle_name") val middleName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("birth_date") val birthDate: String? = null,
    val gender: String? = null
)

internal data class CreateOrderRequest(
    val user: String,
    val status: String,
    @SerializedName("total_price") val totalPrice: Double
)

internal data class UpdateOrderRequest(
    val status: String,
    @SerializedName("total_price") val totalPrice: Double
)

internal data class CreateOrderItemRequest(
    val order: String,
    val product: String,
    val quantity: Int,
    @SerializedName("price_at_purchase") val priceAtPurchase: Double
)

internal data class UpdateQuantityRequest(val quantity: Int)