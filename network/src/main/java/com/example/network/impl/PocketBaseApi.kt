package com.example.network.impl

import com.example.network.model.AuthResponse
import com.example.network.model.BannerDto
import com.example.network.model.CategoryDto
import com.example.network.model.ListResponse
import com.example.network.model.OrderDto
import com.example.network.model.OrderItemDto
import com.example.network.model.ProductDto
import com.example.network.model.ProjectDto
import com.example.network.model.UserDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

/** Описание REST API Pocketbase для Retrofit. Скрыто внутри модуля: наружу видны только сервисы. */
internal interface PocketBaseApi {

    @POST("api/collections/users/auth-with-password")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("api/collections/users/records")
    suspend fun createUser(@Body body: RegisterRequest): UserDto

    @GET("api/collections/users/records/{id}")
    suspend fun getUser(@Path("id") id: String): UserDto

    @PATCH("api/collections/users/records/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body body: UpdateProfileRequest): UserDto

    @GET("api/collections/banners/records")
    suspend fun getBanners(
        @Query("sort") sort: String = "sort_order",
        @Query("perPage") perPage: Int = 200
    ): ListResponse<BannerDto>

    @GET("api/collections/categories/records")
    suspend fun getCategories(@Query("perPage") perPage: Int = 200): ListResponse<CategoryDto>

    @GET("api/collections/products/records")
    suspend fun getProducts(
        @Query("filter") filter: String?,
        @Query("perPage") perPage: Int = 200
    ): ListResponse<ProductDto>

    @GET("api/collections/products/records/{id}")
    suspend fun getProduct(@Path("id") id: String): ProductDto

    @GET("api/collections/orders/records")
    suspend fun getOrders(
        @Query("filter") filter: String,
        @Query("perPage") perPage: Int = 1
    ): ListResponse<OrderDto>

    @POST("api/collections/orders/records")
    suspend fun createOrder(@Body body: CreateOrderRequest): OrderDto

    @PATCH("api/collections/orders/records/{id}")
    suspend fun updateOrder(@Path("id") id: String, @Body body: UpdateOrderRequest): OrderDto

    @GET("api/collections/order_items/records")
    suspend fun getOrderItems(
        @Query("filter") filter: String,
        @Query("expand") expand: String = "product",
        @Query("perPage") perPage: Int = 200
    ): ListResponse<OrderItemDto>

    @POST("api/collections/order_items/records")
    suspend fun createOrderItem(@Body body: CreateOrderItemRequest): OrderItemDto

    @PATCH("api/collections/order_items/records/{id}")
    suspend fun updateOrderItem(@Path("id") id: String, @Body body: UpdateQuantityRequest): OrderItemDto

    @DELETE("api/collections/order_items/records/{id}")
    suspend fun deleteOrderItem(@Path("id") id: String)

    @GET("api/collections/projects/records")
    suspend fun getProjects(
        @Query("filter") filter: String,
        @Query("sort") sort: String = "-created",
        @Query("perPage") perPage: Int = 200
    ): ListResponse<ProjectDto>

    @Multipart
    @POST("api/collections/projects/records")
    suspend fun createProject(
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photo: MultipartBody.Part
    ): ProjectDto
}