package com.example.network.impl

import com.example.network.common.ApiResult
import com.example.network.common.escapeFilter
import com.example.network.common.safeCall
import com.example.network.model.BannerDto
import com.example.network.model.CategoryDto
import com.example.network.model.ProductDto
import com.example.network.service.CatalogService

/** Реализация [CatalogService] поверх Pocketbase. */
internal class CatalogServiceImpl(private val api: PocketBaseApi) : CatalogService {

    override suspend fun getBanners(): ApiResult<List<BannerDto>> = safeCall {
        api.getBanners().items
    }

    override suspend fun getCategories(): ApiResult<List<CategoryDto>> = safeCall {
        api.getCategories().items
    }

    override suspend fun getProducts(categoryId: String?, query: String?): ApiResult<List<ProductDto>> = safeCall {
        val conditions = mutableListOf<String>()
        if (!categoryId.isNullOrBlank()) conditions += "category='${escapeFilter(categoryId)}'"
        if (!query.isNullOrBlank()) conditions += "name~'${escapeFilter(query)}'"
        val filter = if (conditions.isEmpty()) null else conditions.joinToString(" && ")
        api.getProducts(filter).items
    }

    override suspend fun getProduct(id: String): ApiResult<ProductDto> = safeCall {
        api.getProduct(id)
    }
}