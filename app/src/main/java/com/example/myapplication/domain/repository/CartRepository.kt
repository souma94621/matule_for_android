package com.example.myapplication.domain.repository

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Cart

/** Что приложению нужно от корзины. */
interface CartRepository {
    suspend fun getCart(): Resource<Cart>
    suspend fun addToCart(productId: String): Resource<Cart>
    suspend fun removeFromCart(productId: String): Resource<Cart>
    suspend fun setQuantity(itemId: String, quantity: Int): Resource<Cart>
    suspend fun checkout(): Resource<Unit>
}