package com.example.network.service

import com.example.network.common.ApiResult
import com.example.network.model.BannerDto
import com.example.network.model.CategoryDto
import com.example.network.model.ProductDto

/** Акции, категории и каталог описаний. */
interface CatalogService {
    /** Баннеры «Акции и новости» по порядку показа. */
    suspend fun getBanners(): ApiResult<List<BannerDto>>

    /** Список категорий для фильтров. */
    suspend fun getCategories(): ApiResult<List<CategoryDto>>

    /**
     * Список товаров.
     *
     * @param categoryId id категории для фильтра; null — все категории
     * @param query текст поиска по названию; null — без поиска
     */
    suspend fun getProducts(categoryId: String? = null, query: String? = null): ApiResult<List<ProductDto>>

    /** Один товар по id (для BottomSheet с описанием). */
    suspend fun getProduct(id: String): ApiResult<ProductDto>
}