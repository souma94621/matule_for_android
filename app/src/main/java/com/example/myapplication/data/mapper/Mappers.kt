package com.example.myapplication.data.mapper

import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Banner
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.model.User
import com.example.network.common.ApiResult
import com.example.network.model.BannerDto
import com.example.network.model.CategoryDto
import com.example.network.model.ProductDto
import com.example.network.model.UserDto

/** Превращает результат сетевого слоя в результат домена. Единое место (DRY). */
internal inline fun <T, R> ApiResult<T>.toResource(transform: (T) -> R): Resource<R> =
    when (this) {
        is ApiResult.Success -> Resource.Success(transform(data))
        is ApiResult.Error -> Resource.Failure(message)
    }

internal fun UserDto.toDomain() = User(id, email, firstName, middleName, lastName, birthDate, gender)

internal fun CategoryDto.toDomain() = Category(id, name, targetAudience)

internal fun ProductDto.toDomain(imageUrl: String?) = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    categoryId = category,
    imageUrl = imageUrl,
    estimatedConsumption = estimatedConsumption
)

internal fun BannerDto.toDomain(imageUrl: String?) =
    Banner(id, title, priceLabel, imageUrl, product, sortOrder)