package com.example.myapplication.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.di.AppContainer
import com.example.myapplication.presentation.cart.CartScreen
import com.example.myapplication.presentation.cart.CartViewModel
import com.example.myapplication.presentation.catalog.CatalogScreen
import com.example.myapplication.presentation.catalog.CatalogViewModel
import com.example.myapplication.presentation.createproject.CreateProjectScreen
import com.example.myapplication.presentation.createproject.CreateProjectViewModel
import com.example.myapplication.presentation.document.PdfViewerScreen
import com.example.myapplication.presentation.home.HomeScreen
import com.example.myapplication.presentation.home.HomeViewModel
import com.example.myapplication.presentation.profile.ProfileScreen
import com.example.myapplication.presentation.profile.ProfileViewModel
import com.example.myapplication.presentation.projects.ProjectsScreen
import com.example.myapplication.presentation.projects.ProjectsViewModel
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleErrorDialog
import com.example.uikit.components.MatuleTabBar
import com.example.uikit.components.MatuleToast
import kotlinx.coroutines.delay

/** Что открыто поверх вкладки. Значения сохраняются как «последний экран». */
private object Overlay {
    const val NONE = ""
    const val CART = "cart"
    const val CREATE_PROJECT = "create_project"
    const val PRIVACY = "privacy"
    const val TERMS = "terms"
}

/** Основная часть приложения: нижняя панель, вкладки, корзина, создание проекта и документы. */
@Composable
fun MainScreen(container: AppContainer, onLogout: () -> Unit) {
    val lastScreen = container.lastScreenRepository
    var tab by remember { mutableIntStateOf(lastScreen.tab.coerceIn(0, 3)) }
    var overlay by remember { mutableStateOf(lastScreen.overlay) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    // Запоминаем последний экран, чтобы после перезапуска вернуться на него
    LaunchedEffect(tab) { lastScreen.tab = tab }
    LaunchedEffect(overlay) { lastScreen.overlay = overlay }

    val homeViewModel: HomeViewModel = viewModel(
        factory = viewModelFactory {
            initializer { HomeViewModel(container.getHomeUseCase, container.getProductsUseCase) }
        }
    )
    val catalogViewModel: CatalogViewModel = viewModel(
        factory = viewModelFactory {
            initializer { CatalogViewModel(container.getProductsUseCase, container.getCategoriesUseCase) }
        }
    )
    val profileViewModel: ProfileViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ProfileViewModel(container.getProfileUseCase) }
        }
    )
    val projectsViewModel: ProjectsViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ProjectsViewModel(container.getProjectsUseCase, container.reminders) }
        }
    )
    val createProjectViewModel: CreateProjectViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                CreateProjectViewModel(container.getCategoriesUseCase, container.createProjectUseCase)
            }
        }
    )
    val cartViewModel: CartViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                CartViewModel(
                    container.getCartUseCase,
                    container.addToCartUseCase,
                    container.removeFromCartUseCase,
                    container.setCartQuantityUseCase,
                    container.checkoutUseCase
                )
            }
        }
    )
    val cart by cartViewModel.state.collectAsState()
    val createState by createProjectViewModel.state.collectAsState()

    // Заказ оформлен: закрыть корзину, вернуться на «Главную», показать уведомление
    LaunchedEffect(cart.orderPlaced) {
        if (cart.orderPlaced) {
            cartViewModel.onOrderShown()
            overlay = Overlay.NONE
            tab = 0
            toastMessage = "Заказ успешно оформлен!"
        }
    }
    // Проект создан: очистить форму, обновить список, вернуться к списку проектов
    LaunchedEffect(createState.created) {
        if (createState.created) {
            createProjectViewModel.reset()
            projectsViewModel.load()
            overlay = Overlay.NONE
            tab = 2
            toastMessage = "Проект создан"
        }
    }
    // Уведомление исчезает через 5 секунд
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(5000)
            toastMessage = null
        }
    }
    BackHandler(enabled = overlay != Overlay.NONE) { overlay = Overlay.NONE }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Column {
                    if (cart.items.isNotEmpty() && overlay == Overlay.NONE && (tab == 0 || tab == 1)) {
                        MatuleButton(
                            text = "Корзина · ${cart.total.toInt()} ₽",
                            onClick = { overlay = Overlay.CART },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    MatuleTabBar(
                        selectedIndex = tab,
                        onSelect = {
                            tab = it
                            overlay = Overlay.NONE
                        }
                    )
                }
            }
        ) { innerPadding ->
            val contentModifier = Modifier.padding(innerPadding)
            when {
                overlay == Overlay.CART -> CartScreen(
                    state = cart,
                    onQuantityChange = cartViewModel::changeQuantity,
                    onCheckout = cartViewModel::placeOrder,
                    onBack = { overlay = Overlay.NONE },
                    modifier = contentModifier
                )
                overlay == Overlay.CREATE_PROJECT -> CreateProjectScreen(
                    viewModel = createProjectViewModel,
                    onBack = { overlay = Overlay.NONE },
                    modifier = contentModifier
                )
                overlay == Overlay.PRIVACY -> PdfViewerScreen(
                    assetName = "privacy_policy.pdf",
                    title = "Политика конфиденциальности",
                    onBack = { overlay = Overlay.NONE },
                    modifier = contentModifier
                )
                overlay == Overlay.TERMS -> PdfViewerScreen(
                    assetName = "user_agreement.pdf",
                    title = "Пользовательское соглашение",
                    onBack = { overlay = Overlay.NONE },
                    modifier = contentModifier
                )
                tab == 0 -> HomeScreen(
                    viewModel = homeViewModel,
                    cartProductIds = cart.productIds,
                    onToggleCart = cartViewModel::toggle,
                    modifier = contentModifier
                )
                tab == 1 -> CatalogScreen(
                    viewModel = catalogViewModel,
                    cartProductIds = cart.productIds,
                    onToggleCart = cartViewModel::toggle,
                    modifier = contentModifier
                )
                tab == 2 -> ProjectsScreen(
                    viewModel = projectsViewModel,
                    onCreateClick = { overlay = Overlay.CREATE_PROJECT },
                    modifier = contentModifier
                )
                tab == 3 -> ProfileScreen(
                    viewModel = profileViewModel,
                    onOpenPrivacy = { overlay = Overlay.PRIVACY },
                    onOpenTerms = { overlay = Overlay.TERMS },
                    onLogout = onLogout,
                    modifier = contentModifier
                )
                else -> Box(
                    modifier = contentModifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Раздел в разработке")
                }
            }
        }

        toastMessage?.let { message ->
            MatuleToast(
                message = message,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .systemBarsPadding()
                    .padding(16.dp)
            )
        }
    }

    cart.errorMessage?.let { message ->
        MatuleErrorDialog(message = message, onDismiss = cartViewModel::onErrorDismissed)
    }
}