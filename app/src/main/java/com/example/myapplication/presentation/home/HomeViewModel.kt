package com.example.myapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Banner
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.usecase.GetBannersUseCase
import com.example.myapplication.domain.usecase.GetCategoriesUseCase
import com.example.myapplication.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Всё, что нужно экрану «Главная» для отрисовки. */
data class HomeUiState(
    val isLoading: Boolean = true,
    val banners: List<Banner> = emptyList(),
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val selectedCategoryId: String? = null,
    val query: String = "",
    val error: String? = null
)

/** Логика экрана «Главная»: загрузка, фильтр по категории, поиск. */
class HomeViewModel(
    private val getBanners: GetBannersUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val getProducts: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private var productsJob: Job? = null

    init {
        load()
    }

    /** Первичная загрузка (и повтор после ошибки). */
    fun load() {
        viewModelScope.launch {
            _state.value = HomeUiState(isLoading = true)
            val banners = getBanners()
            val categories = getCategories()
            val products = getProducts()
            _state.value = HomeUiState(
                isLoading = false,
                banners = (banners as? Resource.Success)?.data.orEmpty(),
                categories = (categories as? Resource.Success)?.data.orEmpty(),
                products = (products as? Resource.Success)?.data.orEmpty(),
                error = (products as? Resource.Failure)?.message
            )
        }
    }

    /** Выбор чипа категории; null означает «Все». */
    fun onCategorySelected(categoryId: String?) {
        _state.update { it.copy(selectedCategoryId = categoryId) }
        reloadProducts(withDelay = false)
    }

    /** Ввод в поиск (запрос уходит с небольшой задержкой, чтобы не слать его на каждую букву). */
    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
        reloadProducts(withDelay = true)
    }

    private fun reloadProducts(withDelay: Boolean) {
        productsJob?.cancel()
        productsJob = viewModelScope.launch {
            if (withDelay) delay(300)
            val s = _state.value
            when (val result = getProducts(s.selectedCategoryId, s.query.ifBlank { null })) {
                is Resource.Success -> _state.update { it.copy(products = result.data, error = null) }
                is Resource.Failure -> _state.update { it.copy(error = result.message) }
            }
        }
    }
}