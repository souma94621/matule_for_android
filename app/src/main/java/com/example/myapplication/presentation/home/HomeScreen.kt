package com.example.myapplication.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.model.Product
import com.example.myapplication.presentation.common.ProductSheet
import com.example.uikit.components.MatuleBanner
import com.example.uikit.components.MatuleBannerGradients
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleCard
import com.example.uikit.components.MatuleChip
import com.example.uikit.components.MatuleSearchField
import com.example.uikit.theme.MatuleError
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleTextSecondary

/**
 * Экран «Главная»: поиск, акции, фильтры по категориям и список описаний.
 *
 * @param cartProductIds id товаров, которые уже в корзине
 * @param onToggleCart добавить товар в корзину или убрать (в зависимости от текущего состояния)
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    cartProductIds: Set<String>,
    onToggleCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val current = state
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val categoryNames = remember(current.categories) {
        current.categories.associate { it.id to it.name }
    }

    when {
        current.isLoading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MatulePrimary)
        }

        current.error != null && current.products.isEmpty() -> Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = current.error, color = MatuleError)
            MatuleButton(text = "Повторить", onClick = viewModel::load)
        }

        else -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MatuleSearchField(
                    value = current.query,
                    onValueChange = viewModel::onQueryChange,
                    placeholder = "Искать описания"
                )
            }

            if (current.banners.isNotEmpty()) {
                item { SectionTitle("Акции и новости") }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        itemsIndexed(current.banners, key = { _, banner -> banner.id }) { index, banner ->
                            MatuleBanner(
                                title = banner.title,
                                priceLabel = banner.priceLabel,
                                gradient = MatuleBannerGradients[index % MatuleBannerGradients.size]
                            )
                        }
                    }
                }
            }

            item { SectionTitle("Каталог описаний") }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        MatuleChip(
                            text = "Все",
                            selected = current.selectedCategoryId == null,
                            onClick = { viewModel.onCategorySelected(null) }
                        )
                    }
                    items(current.categories, key = { it.id }) { category ->
                        MatuleChip(
                            text = category.name,
                            selected = category.id == current.selectedCategoryId,
                            onClick = { viewModel.onCategorySelected(category.id) }
                        )
                    }
                }
            }

            if (current.products.isEmpty()) {
                item { Text(text = "Ничего не найдено", color = MatuleTextSecondary) }
            }
            items(current.products, key = { it.id }) { product ->
                MatuleCard(
                    title = product.name,
                    category = categoryNames[product.categoryId].orEmpty(),
                    price = "${product.price.toInt()} ₽",
                    isAdded = product.id in cartProductIds,
                    onAddClick = { onToggleCart(product) },
                    modifier = Modifier.clickable { selectedProduct = product }
                )
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

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MatuleTextSecondary,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    )
}