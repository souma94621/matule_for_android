package com.example.myapplication.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.model.CartItem
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleHeader
import com.example.uikit.components.MatuleStepper
import com.example.uikit.theme.MatuleBackground
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleTextSecondary

/** Экран «Корзина»: изменение состава и оформление заказа. */
@Composable
fun CartScreen(
    state: CartUiState,
    onQuantityChange: (CartItem, Int) -> Unit,
    onCheckout: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        MatuleHeader(
            title = "Корзина",
            action = {
                TextButton(onClick = onBack) { Text("Назад", color = MatulePrimary) }
            }
        )
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MatulePrimary)
            }

            state.items.isEmpty() -> Text(
                text = "Корзина пуста",
                color = MatuleTextSecondary,
                modifier = Modifier.padding(16.dp)
            )

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.items, key = { it.id }) { item ->
                        CartRow(item = item, onQuantityChange = onQuantityChange)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Итого", fontSize = 18.sp)
                    Text(text = "${state.total.toInt()} ₽", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                MatuleButton(
                    text = "Перейти к оформлению заказа",
                    onClick = onCheckout,
                    enabled = !state.isBusy,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun CartRow(item: CartItem, onQuantityChange: (CartItem, Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MatuleBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.product.name, fontSize = 17.sp, fontWeight = FontWeight.Medium)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${(item.product.price * item.quantity).toInt()} ₽",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                MatuleStepper(
                    value = item.quantity,
                    onMinus = { onQuantityChange(item, item.quantity - 1) },
                    onPlus = { onQuantityChange(item, item.quantity + 1) }
                )
            }
        }
    }
}