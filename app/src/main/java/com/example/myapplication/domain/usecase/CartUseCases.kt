package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Cart
import com.example.myapplication.domain.repository.CartRepository

/** Получить корзину. */
class GetCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(): Resource<Cart> = repository.getCart()
}

/** Добавить товар в корзину. */
class AddToCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(productId: String): Resource<Cart> = repository.addToCart(productId)
}

/** Убрать товар из корзины. */
class RemoveFromCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(productId: String): Resource<Cart> = repository.removeFromCart(productId)
}

/** Изменить количество позиции (0 и меньше удаляет её). */
class SetCartQuantityUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(itemId: String, quantity: Int): Resource<Cart> =
        repository.setQuantity(itemId, quantity)
}

/** Оформить заказ. */
class CheckoutUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(): Resource<Unit> = repository.checkout()
}