package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.repository.AuthRepositoryImpl
import com.example.myapplication.data.repository.CatalogRepositoryImpl
import com.example.myapplication.data.storage.PinRepositoryImpl
import com.example.myapplication.data.storage.PrefsSessionStorage
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.CatalogRepository
import com.example.myapplication.domain.repository.PinRepository
import com.example.myapplication.domain.usecase.GetBannersUseCase
import com.example.myapplication.domain.usecase.GetCategoriesUseCase
import com.example.myapplication.domain.usecase.GetProductsUseCase
import com.example.myapplication.domain.usecase.GetProfileUseCase
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.LogoutUseCase
import com.example.myapplication.domain.usecase.RegisterUseCase
import com.example.network.MatuleNetwork

/** Собирает все зависимости приложения в одном месте. */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    private val network = MatuleNetwork(
        baseUrl = "http://localhost:8090/",
        session = PrefsSessionStorage(appContext)
    )

    private val authRepository: AuthRepository = AuthRepositoryImpl(network.auth)
    private val catalogRepository: CatalogRepository =
        CatalogRepositoryImpl(network.catalog, network::fileUrl)
    val pinRepository: PinRepository = PinRepositoryImpl(appContext)

    val loginUseCase = LoginUseCase(authRepository)
    val registerUseCase = RegisterUseCase(authRepository)
    val getProfileUseCase = GetProfileUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository, pinRepository)
    val getProductsUseCase = GetProductsUseCase(catalogRepository)
    val getBannersUseCase = GetBannersUseCase(catalogRepository)
    val getCategoriesUseCase = GetCategoriesUseCase(catalogRepository)

    /** True, если пользователь входил раньше и создал PIN: тогда при запуске просим PIN. */
    fun canLoginWithPin(): Boolean = authRepository.isLoggedIn() && pinRepository.hasPin()
}