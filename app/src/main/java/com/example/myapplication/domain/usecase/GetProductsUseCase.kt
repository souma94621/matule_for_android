package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.repository.CatalogRepository

/** Получить список товаров с необязательным фильтром по категории и поиском. */
class GetProductsUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(categoryId: String? = null, query: String? = null): Resource<List<Product>> =
        repository.getProducts(categoryId, query)
}