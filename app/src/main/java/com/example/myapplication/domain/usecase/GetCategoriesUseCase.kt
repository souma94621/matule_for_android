package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.repository.CatalogRepository

/** Получить категории для фильтров. */
class GetCategoriesUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(): Resource<List<Category>> = repository.getCategories()
}