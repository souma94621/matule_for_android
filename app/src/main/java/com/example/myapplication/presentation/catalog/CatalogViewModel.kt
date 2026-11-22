package com.example.myapplication.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.usecase.GetCategoriesUseCase
import com.example.myapplication.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CatalogUiState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val categoryNames: Map<String, String> = emptyMap(),
    val query: String = "",
    val error: String? = null
)

/** Логика экрана «Каталог»: загрузка и поиск. */
class CatalogViewModel(
    private val getProducts: GetProductsUseCase,
    private val getCategories: GetCategoriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogUiState())
    val state: StateFlow<CatalogUiState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = CatalogUiState(isLoading = true)
            val categories = getCategories()
            val products = getProducts()
            _state.value = CatalogUiState(
                isLoading = false,
                products = (products as? Resource.Success)?.data.orEmpty(),
                categoryNames = (categories as? Resource.Success)?.data.orEmpty().associate { it.id to it.name },
                error = (products as? Resource.Failure)?.message
            )
        }
    }

    /** Ввод в поиск; запрос уходит с задержкой 300 мс. */
    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            when (val result = getProducts(null, query.ifBlank { null })) {
                is Resource.Success -> _state.update { it.copy(products = result.data, error = null) }
                is Resource.Failure -> _state.update { it.copy(error = result.message) }
            }
        }
    }
}