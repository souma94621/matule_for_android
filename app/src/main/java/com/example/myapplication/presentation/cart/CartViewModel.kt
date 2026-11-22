package com.example.myapplication.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Cart
import com.example.myapplication.domain.model.CartItem
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.usecase.AddToCartUseCase
import com.example.myapplication.domain.usecase.CheckoutUseCase
import com.example.myapplication.domain.usecase.GetCartUseCase
import com.example.myapplication.domain.usecase.RemoveFromCartUseCase
import com.example.myapplication.domain.usecase.SetCartQuantityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartUiState(
    val isLoading: Boolean = true,
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val isBusy: Boolean = false,
    val errorMessage: String? = null,
    val orderPlaced: Boolean = false
) {
    /** id товаров в корзине, чтобы экраны каталога знали, какие карточки показывать как добавленные. */
    val productIds: Set<String> get() = items.map { it.product.id }.toSet()
}

/** Состояние корзины, общее для «Главной», «Каталога» и экрана «Корзина». */
class CartViewModel(
    private val getCart: GetCartUseCase,
    private val addToCart: AddToCartUseCase,
    private val removeFromCart: RemoveFromCartUseCase,
    private val setQuantity: SetCartQuantityUseCase,
    private val checkout: CheckoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CartUiState())
    val state: StateFlow<CartUiState> = _state.asStateFlow()

    init {
        run { getCart() }
    }

    /** Добавляет товар или убирает, если он уже в корзине. */
    fun toggle(product: Product) {
        if (product.id in _state.value.productIds) {
            run { removeFromCart(product.id) }
        } else {
            run { addToCart(product.id) }
        }
    }

    /** Задаёт новое количество позиции (0 удаляет её). */
    fun changeQuantity(item: CartItem, newQuantity: Int) {
        run { setQuantity(item.id, newQuantity) }
    }

    fun placeOrder() {
        if (_state.value.isBusy) return
        viewModelScope.launch {
            _state.update { it.copy(isBusy = true) }
            when (val result = checkout()) {
                is Resource.Success -> _state.update {
                    it.copy(isBusy = false, items = emptyList(), total = 0.0, orderPlaced = true)
                }
                is Resource.Failure -> _state.update {
                    it.copy(isBusy = false, errorMessage = result.message)
                }
            }
        }
    }

    fun onErrorDismissed() {
        _state.update { it.copy(errorMessage = null) }
    }

    /** Экран показал уведомление об успехе: сбрасываем флаг. */
    fun onOrderShown() {
        _state.update { it.copy(orderPlaced = false) }
    }

    /** Выполняет одно действие над корзиной; пока оно идёт, новые нажатия игнорируются. */
    private fun run(block: suspend () -> Resource<Cart>) {
        if (_state.value.isBusy) return
        viewModelScope.launch {
            _state.update { it.copy(isBusy = true) }
            when (val result = block()) {
                is Resource.Success -> _state.update {
                    it.copy(isBusy = false, isLoading = false, items = result.data.items, total = result.data.total)
                }
                is Resource.Failure -> _state.update {
                    it.copy(isBusy = false, isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
}