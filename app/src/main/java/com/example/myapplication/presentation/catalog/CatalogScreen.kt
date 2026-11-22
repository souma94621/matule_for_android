package com.example.myapplication.presentation.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.Product
import com.example.myapplication.presentation.common.ProductSheet
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleCard
import com.example.uikit.components.MatuleHeader
import com.example.uikit.components.MatuleSearchField
import com.example.uikit.theme.MatuleError
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleTextSecondary

/** Экран «Каталог»: поиск и список с добавлением и удалением из корзины. */
@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    cartProductIds: Set<String>,
    onToggleCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val current = state
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    Column(modifier = modifier) {
        MatuleHeader(title = "Каталог")
        MatuleSearchField(
            value = current.query,
            onValueChange = viewModel::onQueryChange,
            placeholder = "Искать описания",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        when {
            current.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MatulePrimary)
            }

            current.error != null && current.products.isEmpty() -> Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = current.error, color = MatuleError)
                MatuleButton(text = "Повторить", onClick = viewModel::load)
            }

            else -> LazyColumn(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (current.products.isEmpty()) {
                    item { Text(text = "Ничего не найдено", color = MatuleTextSecondary) }
                }
                items(current.products, key = { it.id }) { product ->
                    MatuleCard(
                        title = product.name,
                        category = current.categoryNames[product.categoryId].orEmpty(),
                        price = "${product.price.toInt()} ₽",
                        isAdded = product.id in cartProductIds,
                        onAddClick = { onToggleCart(product) },
                        modifier = Modifier.clickable { selectedProduct = product }
                    )
                }
            }
        }
    }

    selectedProduct?.let { product ->
        ProductSheet(
            product = product,
            isAdded = product.id in cartProductIds,
            onToggle = { onToggleCart(product) },
            onDismiss = { selectedProduct = null }
        )
    }
}