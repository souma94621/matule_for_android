package com.example.myapplication.data.repository

import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.data.mapper.toResource
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Banner
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.repository.CatalogRepository
import com.example.network.service.CatalogService

/**
 * Реализация [CatalogRepository] поверх [CatalogService].
 *
 * @param imageUrl строит ссылку на файл по (collectionId, recordId, fileName)
 */
class CatalogRepositoryImpl(
    private val service: CatalogService,
    private val imageUrl: (String, String, String) -> String
) : CatalogRepository {

    override suspend fun getBanners(): Resource<List<Banner>> =
        service.getBanners().toResource { list ->
            list.map { dto ->
                val url = if (dto.image.isBlank()) null else imageUrl(dto.collectionId, dto.id, dto.image)
                dto.toDomain(url)
            }
        }

    override suspend fun getProducts(categoryId: String?, query: String?): Resource<List<Product>> =
        service.getProducts(categoryId, query).toResource { list ->
            list.map { dto ->
                val url = if (dto.image.isBlank()) null else imageUrl(dto.collectionId, dto.id, dto.image)
                dto.toDomain(url)
            }
        }

    override suspend fun getCategories(): Resource<List<Category>> =
        service.getCategories().toResource { list -> list.map { it.toDomain() } }
}