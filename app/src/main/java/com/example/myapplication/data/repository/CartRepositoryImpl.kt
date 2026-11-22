package com.example.myapplication.data.repository

import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.data.mapper.toResource
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Cart
import com.example.myapplication.domain.repository.CartRepository
import com.example.network.service.CartService

/** Реализация [CartRepository] поверх сетевого интерфейса [CartService]. */
class CartRepositoryImpl(private val service: CartService) : CartRepository {

    override suspend fun getCart(): Resource<Cart> =
        service.getCart().toResource { it.toDomain() }

    override suspend fun addToCart(productId: String): Resource<Cart> =
        service.addToCart(productId).toResource { it.toDomain() }

    override suspend fun removeFromCart(productId: String): Resource<Cart> =
        service.removeFromCart(productId).toResource { it.toDomain() }

    override suspend fun setQuantity(itemId: String, quantity: Int): Resource<Cart> =
        service.setQuantity(itemId, quantity).toResource { it.toDomain() }

    override suspend fun checkout(): Resource<Unit> =
        service.checkout().toResource { }
}