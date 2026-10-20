package com.example.network.impl

import com.example.network.common.SessionStorage
import okhttp3.Interceptor
import okhttp3.Response

/** Добавляет токен из сессии в заголовок Authorization каждого запроса. */
internal class AuthInterceptor(private val session: SessionStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = session.token
        val request = if (token != null) {
            chain.request().newBuilder().header("Authorization", token).build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}