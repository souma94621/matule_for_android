package com.example.myapplication.presentation.navigation

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.di.AppContainer
import com.example.myapplication.presentation.login.LoginScreen
import com.example.myapplication.presentation.login.LoginViewModel
import com.example.myapplication.presentation.main.MainScreen
import com.example.myapplication.presentation.pin.PinMode
import com.example.myapplication.presentation.pin.PinScreen
import com.example.myapplication.presentation.pin.PinViewModel
import com.example.myapplication.presentation.registration.RegistrationPasswordScreen
import com.example.myapplication.presentation.registration.RegistrationProfileScreen
import com.example.myapplication.presentation.registration.RegistrationViewModel
import com.example.myapplication.presentation.splash.SplashScreen

/** Адреса экранов. */
object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REG_GRAPH = "reg_graph"
    const val REG_PROFILE = "reg_profile"
    const val REG_PASSWORD = "reg_password"
    const val PIN_CREATE = "pin_create"
    const val PIN_ENTER = "pin_enter"
    const val MAIN = "main"
}

/** Граф навигации приложения. */
@Composable
fun AppNavigation(container: AppContainer) {
    val navController = rememberNavController()
    val screenModifier = Modifier.systemBarsPadding().imePadding()

    // Запоминаем экраны входа и регистрации, чтобы после перезапуска вернуться на них
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            if (route == Routes.LOGIN || route == Routes.REG_PROFILE || route == Routes.REG_PASSWORD) {
                container.lastScreenRepository.route = route
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    val loggedIn = container.isLoggedIn()
                    val hasPin = container.pinRepository.hasPin()
                    val last = container.lastScreenRepository.route
                    when {
                        loggedIn && hasPin -> navController.navigate(Routes.PIN_ENTER) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                        loggedIn -> navController.navigate(Routes.PIN_CREATE) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                        else -> {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                            // Пользователь не закончил регистрацию: возвращаем его на тот же экран
                            if (last == Routes.REG_PROFILE || last == Routes.REG_PASSWORD) {
                                navController.navigate(Routes.REG_GRAPH)
                                if (last == Routes.REG_PASSWORD) navController.navigate(Routes.REG_PASSWORD)
                            }
                        }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            val viewModel: LoginViewModel = viewModel(
                factory = viewModelFactory { initializer { LoginViewModel(container.loginUseCase) } }
            )
            LoginScreen(
                viewModel = viewModel,
                onLoggedIn = {
                    val next = if (container.pinRepository.hasPin()) Routes.MAIN else Routes.PIN_CREATE
                    navController.navigate(next) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onRegisterClick = { navController.navigate(Routes.REG_GRAPH) },
                modifier = screenModifier
            )
        }

        navigation(startDestination = Routes.REG_PROFILE, route = Routes.REG_GRAPH) {
            composable(Routes.REG_PROFILE) { entry ->
                val parent = remember(entry) { navController.getBackStackEntry(Routes.REG_GRAPH) }
                val viewModel: RegistrationViewModel = viewModel(
                    viewModelStoreOwner = parent,
                    factory = viewModelFactory {
                        initializer {
                            RegistrationViewModel(container.registerUseCase, container.registrationDraftRepository)
                        }
                    }
                )
                RegistrationProfileScreen(
                    viewModel = viewModel,
                    onNext = { navController.navigate(Routes.REG_PASSWORD) },
                    modifier = screenModifier
                )
            }
            composable(Routes.REG_PASSWORD) { entry ->
                val parent = remember(entry) { navController.getBackStackEntry(Routes.REG_GRAPH) }
                val viewModel: RegistrationViewModel = viewModel(
                    viewModelStoreOwner = parent,
                    factory = viewModelFactory {
                        initializer {
                            RegistrationViewModel(container.registerUseCase, container.registrationDraftRepository)
                        }
                    }
                )
                RegistrationPasswordScreen(
                    viewModel = viewModel,
                    onRegistered = {
                        navController.navigate(Routes.PIN_CREATE) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    modifier = screenModifier
                )
            }
        }

        composable(Routes.PIN_CREATE) {
            val viewModel: PinViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { PinViewModel(PinMode.CREATE, container.pinRepository) }
                }
            )
            PinScreen(
                viewModel = viewModel,
                onDone = {
                    navController.navigate(Routes.MAIN) { popUpTo(Routes.PIN_CREATE) { inclusive = true } }
                },
                modifier = screenModifier
            )
        }

        composable(Routes.PIN_ENTER) {
            val viewModel: PinViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { PinViewModel(PinMode.ENTER, container.pinRepository) }
                }
            )
            PinScreen(
                viewModel = viewModel,
                onDone = {
                    navController.navigate(Routes.MAIN) { popUpTo(Routes.PIN_ENTER) { inclusive = true } }
                },
                onForgot = {
                    container.logoutUseCase()
                    navController.navigate(Routes.LOGIN) { popUpTo(Routes.PIN_ENTER) { inclusive = true } }
                },
                modifier = screenModifier
            )
        }

        composable(Routes.MAIN) {
            MainScreen(
                container = container,
                onLogout = {
                    container.logoutUseCase()
                    navController.navigate(Routes.LOGIN) { popUpTo(Routes.MAIN) { inclusive = true } }
                }
            )
        }
    }
}