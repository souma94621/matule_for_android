package com.example.network.impl

import com.example.network.common.ApiResult
import com.example.network.common.SessionStorage
import com.example.network.common.escapeFilter
import com.example.network.common.safeCall
import com.example.network.model.Cart
import com.example.network.model.CartItem
import com.example.network.model.OrderDto
import com.example.network.service.CartService

/** Реализация [CartService]: корзина это заказ со статусом cart, позиции лежат в order_items. */
internal class CartServiceImpl(
    private val api: PocketBaseApi,
    private val session: SessionStorage
) : CartService {

    override suspend fun getCart(): ApiResult<Cart> = safeCall {
        loadCart(getOrCreateCartOrder())
    }

    override suspend fun addToCart(productId: String): ApiResult<Cart> = safeCall {
        val order = getOrCreateCartOrder()
        val existing = findItem(order.id, productId)
        if (existing == null) {
            val product = api.getProduct(productId)
            api.createOrderItem(CreateOrderItemRequest(order.id, productId, 1, product.price))
        } else {
            api.updateOrderItem(existing.id, UpdateQuantityRequest(existing.quantity + 1))
        }
        loadCart(order)
    }

    override suspend fun removeFromCart(productId: String): ApiResult<Cart> = safeCall {
        val order = getOrCreateCartOrder()
        findItem(order.id, productId)?.let { api.deleteOrderItem(it.id) }
        loadCart(order)
    }

    override suspend fun setQuantity(itemId: String, quantity: Int): ApiResult<Cart> = safeCall {
        if (quantity <= 0) {
            api.deleteOrderItem(itemId)
        } else {
            api.updateOrderItem(itemId, UpdateQuantityRequest(quantity))
        }
        loadCart(getOrCreateCartOrder())
    }

    override suspend fun checkout(): ApiResult<Unit> = safeCall {
        val order = getOrCreateCartOrder()
        val cart = loadCart(order)
        if (cart.items.isEmpty()) error("Корзина пуста")
        api.updateOrder(order.id, UpdateOrderRequest(status = "new", totalPrice = cart.total))
        Unit
    }

    /** Находит заказ-корзину пользователя или создаёт новый. */
    private suspend fun getOrCreateCartOrder(): OrderDto {
        val userId = session.userId ?: error("Пользователь не авторизован")
        val filter = "user='${escapeFilter(userId)}' && status='cart'"
        return api.getOrders(filter).items.firstOrNull()
            ?: api.createOrder(CreateOrderRequest(user = userId, status = "cart", totalPrice = 0.0))
    }

    /** Ищет позицию заказа с нужным товаром. */
    private suspend fun findItem(orderId: String, productId: String) =
        api.getOrderItems(
            "order='${escapeFilter(orderId)}' && product='${escapeFilter(productId)}'"
        ).items.firstOrNull()

    /** Собирает корзину: подгружает позиции вместе с товарами и считает сумму. */
    private suspend fun loadCart(order: OrderDto): Cart {
        val items = api.getOrderItems("order='${escapeFilter(order.id)}'").items.mapNotNull { item ->
            val product = item.expand?.product ?: return@mapNotNull null
            CartItem(item.id, product, item.quantity)
        }
        return Cart(order.id, items, items.sumOf { it.product.price * it.quantity })
    }
}