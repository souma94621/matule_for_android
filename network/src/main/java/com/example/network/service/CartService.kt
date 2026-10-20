package com.example.network.service

import com.example.network.common.ApiResult
import com.example.network.model.Cart

/** Корзина и оформление заказа. Корзина это заказ со статусом cart. */
interface CartService {
    /** Текущая корзина (создаётся, если её ещё нет). */
    suspend fun getCart(): ApiResult<Cart>

    /** Добавляет товар в корзину; если он уже есть, увеличивает количество на 1. */
    suspend fun addToCart(productId: String): ApiResult<Cart>

    /** Убирает товар из корзины полностью. */
    suspend fun removeFromCart(productId: String): ApiResult<Cart>

    /** Меняет количество позиции; значение 0 и меньше удаляет позицию. */
    suspend fun setQuantity(itemId: String, quantity: Int): ApiResult<Cart>

    /** Оформляет заказ: переводит корзину в статус new. */
    suspend fun checkout(): ApiResult<Unit>
}