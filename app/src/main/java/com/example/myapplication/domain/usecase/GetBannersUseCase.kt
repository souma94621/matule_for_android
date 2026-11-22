package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Banner
import com.example.myapplication.domain.repository.CatalogRepository

/** Получить баннеры «Акции и новости». */
class GetBannersUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(): Resource<List<Banner>> = repository.getBanners()
}