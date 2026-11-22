package com.example.myapplication.domain.repository

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Banner
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.model.Product

/** Что приложению нужно от каталога. */
interface CatalogRepository {
    suspend fun getBanners(): Resource<List<Banner>>
    suspend fun getProducts(categoryId: String? = null, query: String? = null): Resource<List<Product>>
    suspend fun getCategories(): Resource<List<Category>>
}