package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.network.MatuleNetwork
import com.example.network.common.ApiResult
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleCard
import com.example.uikit.components.MatuleHeader
import com.example.uikit.components.MatuleOutlinedButton
import com.example.uikit.components.MatuleSearchField
import com.example.uikit.components.MatuleSelect
import com.example.uikit.components.MatuleTabBar
import com.example.uikit.components.MatuleTextField
import com.example.uikit.theme.MatuleTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DemoScreen(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun DemoScreen(modifier: Modifier = Modifier) {
    val network = remember { MatuleNetwork(baseUrl = "http://localhost:8090/") }
    var networkStatus by remember { mutableStateOf("Загрузка...") }
    LaunchedEffect(Unit) {
        networkStatus = when (val result = network.catalog.getProducts()) {
            is ApiResult.Success -> "Товаров на сервере: ${result.data.size}"
            is ApiResult.Error -> "Ошибка: ${result.message}"
        }
    }

    var query by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<String?>(null) }
    var tab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MatuleHeader(title = "Проекты")
        Text(text = networkStatus)
        MatuleSearchField(
            value = query,
            onValueChange = { query = it },
            placeholder = "Искать описания"
        )
        MatuleCard(
            title = "Рубашка Воскресенье для машинного вязания",
            category = "Мужская одежда",
            price = "300 ₽",
            onAddClick = {}
        )
        MatuleTextField(
            value = email,
            onValueChange = { email = it },
            label = "Вход по E-mail",
            placeholder = "example@mail.com",
            errorText = if (email.isNotEmpty() && !email.contains("@")) "Неверный формат e-mail" else null
        )
        MatuleTextField(
            value = password,
            onValueChange = { password = it },
            label = "Пароль",
            placeholder = "Введите пароль",
            isPassword = true
        )
        MatuleSelect(
            label = "Пол",
            placeholder = "Пол",
            options = listOf("👨 Мужской", "👩 Женский"),
            selected = gender,
            onSelected = { gender = it }
        )
        MatuleButton(
            text = "Далее",
            onClick = {},
            enabled = email.isNotEmpty() && password.isNotEmpty()
        )
        MatuleOutlinedButton(text = "Войти с VK", onClick = {})
        MatuleTabBar(
            selectedIndex = tab,
            onSelect = { tab = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DemoScreenPreview() {
    MatuleTheme {
        DemoScreen()
    }
}