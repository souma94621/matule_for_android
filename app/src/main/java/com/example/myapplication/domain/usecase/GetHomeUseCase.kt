package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.HomeData
import com.example.myapplication.domain.repository.CatalogRepository
import com.example.myapplication.domain.repository.HomeCache

/**
 * Загружает данные главной страницы. Если загрузка удалась, обновляет кэш;
 * если нет сети, отдаёт сохранённые данные (с пометкой fromCache).
 */
class GetHomeUseCase(
    private val catalog: CatalogRepository,
    private val cache: HomeCache
) {
    suspend operator fun invoke(): Resource<HomeData> {
        val banners = catalog.getBanners()
        val categories = catalog.getCategories()
        val products = catalog.getProducts()

        return when (products) {
            is Resource.Success -> {
                val data = HomeData(
                    banners = (banners as? Resource.Success)?.data.orEmpty(),
                    categories = (categories as? Resource.Success)?.data.orEmpty(),
                    products = products.data
                )
                if (banners is Resource.Success && categories is Resource.Success) {
                    cache.save(data)
                }
                Resource.Success(data)
            }
            is Resource.Failure -> {
                val cached = cache.load()
                if (cached != null) Resource.Success(cached.copy(fromCache = true)) else products
            }
        }
    }
}