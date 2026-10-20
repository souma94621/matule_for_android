package com.example.uikit.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.uikit.theme.MatuleBackground
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleTextSecondary

/**
 * Один пункт нижней панели.
 *
 * @param label подпись под иконкой
 * @param icon иконка пункта
 */
data class MatuleTabItem(
    val label: String,
    val icon: ImageVector
)

/** Стандартный набор из четырёх вкладок приложения Matule. */
val MatuleDefaultTabs = listOf(
    MatuleTabItem("Главная", Icons.Default.Home),
    MatuleTabItem("Каталог", Icons.Default.Menu),
    MatuleTabItem("Проекты", Icons.Default.Check),
    MatuleTabItem("Профиль", Icons.Default.Person)
)

/**
 * Нижняя панель навигации Matule. Активная вкладка синяя, остальные серые.
 *
 * @param items список вкладок
 * @param selectedIndex индекс активной вкладки
 * @param onSelect вызывается при нажатии на вкладку, получает её индекс
 * @param modifier модификатор для внешнего управления
 */
@Composable
fun MatuleTabBar(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<MatuleTabItem> = MatuleDefaultTabs
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MatuleBackground
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MatulePrimary,
                    selectedTextColor = MatulePrimary,
                    unselectedIconColor = MatuleTextSecondary,
                    unselectedTextColor = MatuleTextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}