package com.example.myapplication.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.di.AppContainer
import com.example.myapplication.presentation.home.HomeScreen
import com.example.myapplication.presentation.home.HomeViewModel
import com.example.myapplication.presentation.profile.ProfileScreen
import com.example.myapplication.presentation.profile.ProfileViewModel
import com.example.uikit.components.MatuleTabBar

/** Основная часть приложения: нижняя панель и вкладки. */
@Composable
fun MainScreen(container: AppContainer, onLogout: () -> Unit) {
    var tab by remember { mutableIntStateOf(0) }

    val homeViewModel: HomeViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    container.getBannersUseCase,
                    container.getCategoriesUseCase,
                    container.getProductsUseCase
                )
            }
        }
    )
    val profileViewModel: ProfileViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ProfileViewModel(container.getProfileUseCase) }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { MatuleTabBar(selectedIndex = tab, onSelect = { tab = it }) }
    ) { innerPadding ->
        when (tab) {
            0 -> HomeScreen(viewModel = homeViewModel, modifier = Modifier.padding(innerPadding))
            3 -> ProfileScreen(
                viewModel = profileViewModel,
                onLogout = onLogout,
                modifier = Modifier.padding(innerPadding)
            )
            else -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Раздел в разработке")
            }
        }
    }
}