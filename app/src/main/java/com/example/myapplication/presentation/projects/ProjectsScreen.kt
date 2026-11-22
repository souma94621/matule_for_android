package com.example.myapplication.presentation.projects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.model.Project
import com.example.myapplication.domain.util.formatDaysAgo
import com.example.myapplication.domain.util.formatIsoDate
import com.example.uikit.components.MatuleBottomSheet
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleHeader
import com.example.uikit.components.MatuleProjectCard
import com.example.uikit.theme.MatuleError
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleTextSecondary

/** Экран «Проекты»: список и кнопка «+» для создания. «Открыть» показывает шторку с данными проекта. */
@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val current = state
    var opened by remember { mutableStateOf<Project?>(null) }

    Column(modifier = modifier) {
        MatuleHeader(
            title = "Проекты",
            action = {
                TextButton(onClick = onCreateClick) {
                    Text("+", fontSize = 28.sp, color = MatulePrimary)
                }
            }
        )
        when {
            current.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MatulePrimary)
            }

            current.error != null -> Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = current.error, color = MatuleError)
                MatuleButton(text = "Повторить", onClick = viewModel::load)
            }

            current.projects.isEmpty() -> Text(
                text = "Проектов пока нет. Нажмите «+», чтобы создать первый.",
                color = MatuleTextSecondary,
                modifier = Modifier.padding(16.dp)
            )

            else -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(current.projects, key = { it.id }) { project ->
                    MatuleProjectCard(
                        title = project.title,
                        subtitle = formatDaysAgo(project.createdAt),
                        onOpen = {
                            opened = project
                            viewModel.onProjectOpened(project)
                        }
                    )
                }
            }
        }
    }

    opened?.let { project ->
        MatuleBottomSheet(onDismiss = { opened = null }, title = project.title) {
            Info("Тип", project.type)
            Info("Начало", formatIsoDate(project.startDate))
            Info("Окончание", formatIsoDate(project.endDate))
            Info("Кому", project.recipient)
            Info("Источник описания", project.descriptionSource)
        }
    }
}

@Composable
private fun Info(label: String, value: String) {
    Text(text = label, color = MatuleTextSecondary)
    Text(text = value, modifier = Modifier.padding(bottom = 12.dp))
}