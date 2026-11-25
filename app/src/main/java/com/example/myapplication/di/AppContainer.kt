package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.notification.AndroidReminderScheduler
import com.example.myapplication.data.repository.AuthRepositoryImpl
import com.example.myapplication.data.repository.CartRepositoryImpl
import com.example.myapplication.data.repository.CatalogRepositoryImpl
import com.example.myapplication.data.repository.ProjectRepositoryImpl
import com.example.myapplication.data.storage.LastScreenRepositoryImpl
import com.example.myapplication.data.storage.PinRepositoryImpl
import com.example.myapplication.data.storage.PrefsHomeCache
import com.example.myapplication.data.storage.PrefsSessionStorage
import com.example.myapplication.data.storage.RegistrationDraftRepositoryImpl
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.CartRepository
import com.example.myapplication.domain.repository.CatalogRepository
import com.example.myapplication.domain.repository.HomeCache
import com.example.myapplication.domain.repository.LastScreenRepository
import com.example.myapplication.domain.repository.PinRepository
import com.example.myapplication.domain.repository.ProjectRepository
import com.example.myapplication.domain.repository.RegistrationDraftRepository
import com.example.myapplication.domain.repository.ReminderScheduler
import com.example.myapplication.domain.usecase.AddToCartUseCase
import com.example.myapplication.domain.usecase.CheckoutUseCase
import com.example.myapplication.domain.usecase.CreateProjectUseCase
import com.example.myapplication.domain.usecase.GetBannersUseCase
import com.example.myapplication.domain.usecase.GetCartUseCase
import com.example.myapplication.domain.usecase.GetCategoriesUseCase
import com.example.myapplication.domain.usecase.GetHomeUseCase
import com.example.myapplication.domain.usecase.GetProductsUseCase
import com.example.myapplication.domain.usecase.GetProfileUseCase
import com.example.myapplication.domain.usecase.GetProjectsUseCase
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.LogoutUseCase
import com.example.myapplication.domain.usecase.RegisterUseCase
import com.example.myapplication.domain.usecase.RemoveFromCartUseCase
import com.example.myapplication.domain.usecase.SetCartQuantityUseCase
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
    private val cartRepository: CartRepository = CartRepositoryImpl(network.cart)
    private val projectRepository: ProjectRepository =
        ProjectRepositoryImpl(network.projects, network::fileUrl)
    private val homeCache: HomeCache = PrefsHomeCache(appContext)

    val pinRepository: PinRepository = PinRepositoryImpl(appContext)
    val reminders: ReminderScheduler = AndroidReminderScheduler(appContext)
    val lastScreenRepository: LastScreenRepository = LastScreenRepositoryImpl(appContext)
    val registrationDraftRepository: RegistrationDraftRepository = RegistrationDraftRepositoryImpl(appContext)

    val loginUseCase = LoginUseCase(authRepository)
    val registerUseCase = RegisterUseCase(authRepository)
    val getProfileUseCase = GetProfileUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository, pinRepository, reminders, lastScreenRepository)

    val getProductsUseCase = GetProductsUseCase(catalogRepository)
    val getBannersUseCase = GetBannersUseCase(catalogRepository)
    val getCategoriesUseCase = GetCategoriesUseCase(catalogRepository)
    val getHomeUseCase = GetHomeUseCase(catalogRepository, homeCache)

    val getCartUseCase = GetCartUseCase(cartRepository)
    val addToCartUseCase = AddToCartUseCase(cartRepository)
    val removeFromCartUseCase = RemoveFromCartUseCase(cartRepository)
    val setCartQuantityUseCase = SetCartQuantityUseCase(cartRepository)
    val checkoutUseCase = CheckoutUseCase(cartRepository)

    val getProjectsUseCase = GetProjectsUseCase(projectRepository)
    val createProjectUseCase = CreateProjectUseCase(projectRepository)

    /** true, если в сессии есть токен (пользователь входил раньше). */
    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()
}