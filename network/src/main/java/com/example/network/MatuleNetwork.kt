package com.example.network

import com.example.network.common.InMemorySessionStorage
import com.example.network.common.SessionStorage
import com.example.network.impl.AuthInterceptor
import com.example.network.impl.AuthServiceImpl
import com.example.network.impl.CartServiceImpl
import com.example.network.impl.CatalogServiceImpl
import com.example.network.impl.PocketBaseApi
import com.example.network.impl.ProjectServiceImpl
import com.example.network.service.AuthService
import com.example.network.service.CartService
import com.example.network.service.CatalogService
import com.example.network.service.ProjectService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Единая точка входа в сетевой слой. Приложение создаёт один объект и дальше
 * работает только с интерфейсами сервисов, не зная про Retrofit и Pocketbase.
 *
 * @param baseUrl адрес сервера (для эмулятора: http://10.0.2.2:8090/)
 * @param session хранилище сессии; по умолчанию в памяти
 * @param enableLogging писать запросы и ответы в Logcat (для отладки)
 */
class MatuleNetwork(
    private val baseUrl: String,
    val session: SessionStorage = InMemorySessionStorage(),
    enableLogging: Boolean = true
) {
    private val api: PocketBaseApi = buildApi(enableLogging)

    /** Вход, регистрация, профиль. */
    val auth: AuthService = AuthServiceImpl(api, session)

    /** Акции, категории, каталог. */
    val catalog: CatalogService = CatalogServiceImpl(api)

    /** Корзина и оформление заказа. */
    val cart: CartService = CartServiceImpl(api, session)

    /** Проекты. */
    val projects: ProjectService = ProjectServiceImpl(api, session)

    /** Собирает ссылку на файл (картинку) записи Pocketbase. */
    fun fileUrl(collectionId: String, recordId: String, fileName: String): String =
        "${baseUrl.trimEnd('/')}/api/files/$collectionId/$recordId/$fileName"

    private fun buildApi(enableLogging: Boolean): PocketBaseApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(session))
            .apply {
                if (enableLogging) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                            redactHeader("Authorization")
                        }
                    )
                }
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl.trimEnd('/') + "/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PocketBaseApi::class.java)
    }
}