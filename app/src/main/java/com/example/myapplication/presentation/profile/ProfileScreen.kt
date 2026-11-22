package com.example.myapplication.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.components.MatuleButton
import com.example.uikit.theme.MatuleError
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleTextSecondary

/** Экран «Профиль». Пункты «Мои заказы», документы и переключатель уведомлений пока без действий. */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val current = state
    var notifications by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        when {
            current.isLoading -> Text("Загрузка...")
            current.error != null -> {
                Text(text = current.error, color = MatuleError)
                Spacer(modifier = Modifier.padding(8.dp))
                MatuleButton(text = "Повторить", onClick = viewModel::load)
            }
            current.user != null -> {
                Text(text = current.user.firstName, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(text = current.user.email, color = MatuleTextSecondary)
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = "📋  Мои заказы", fontSize = 18.sp, modifier = Modifier.padding(vertical = 12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "⚙️  Уведомления", fontSize = 18.sp)
            Switch(
                checked = notifications,
                onCheckedChange = { notifications = it },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MatulePrimary,
                    checkedThumbColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Политика конфиденциальности",
            color = MatuleTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        Text(
            text = "Пользовательское соглашение",
            color = MatuleTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        Text(
            text = "Выход",
            color = MatuleError,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onLogout)
                .padding(vertical = 8.dp)
        )
    }
}