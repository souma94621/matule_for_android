package com.example.myapplication.presentation.createproject

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.PhotoData
import com.example.uikit.components.MatuleBottomSheet
import com.example.uikit.components.MatuleButton
import com.example.uikit.components.MatuleErrorDialog
import com.example.uikit.components.MatuleHeader
import com.example.uikit.components.MatulePhotoBox
import com.example.uikit.components.MatuleSelect
import com.example.uikit.components.MatuleTextField
import com.example.uikit.theme.MatulePrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Экран «Создать проект»: все поля обязательны; фото выбирается через шторку (камера или галерея). */
@Composable
fun CreateProjectScreen(
    viewModel: CreateProjectViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showPhotoSheet by remember { mutableStateOf(false) }

    fun setPhoto(read: () -> PhotoData?) {
        scope.launch {
            val newPhoto = withContext(Dispatchers.IO) { read() }
            if (newPhoto != null) viewModel.update { copy(photo = newPhoto) }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) setPhoto { PhotoReader.fromUri(context, uri) }
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) setPhoto { PhotoReader.fromBitmap(bitmap) }
    }

    val preview = remember(state.photo) {
        state.photo?.let { BitmapFactory.decodeByteArray(it.bytes, 0, it.bytes.size)?.asImageBitmap() }
    }

    Column(
        modifier = modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        MatuleHeader(
            title = "Создать проект",
            action = { TextButton(onClick = onBack) { Text("Назад", color = MatulePrimary) } }
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MatuleSelect(
                label = "Тип",
                placeholder = "Выберите тип",
                options = PROJECT_TYPES,
                selected = state.type,
                onSelected = { v -> viewModel.update { copy(type = v) } }
            )
            MatuleTextField(
                value = state.title,
                onValueChange = { v -> viewModel.update { copy(title = v) } },
                label = "Название проекта",
                placeholder = "Введите имя"
            )
            MatuleTextField(
                value = state.startDate,
                onValueChange = { v -> viewModel.update { copy(startDate = v) } },
                label = "Дата начала",
                placeholder = "дд.мм.гггг",
                errorText = state.startError
            )
            MatuleTextField(
                value = state.endDate,
                onValueChange = { v -> viewModel.update { copy(endDate = v) } },
                label = "Дата Окончания",
                placeholder = "дд.мм.гггг",
                errorText = state.endError
            )
            MatuleSelect(
                label = "Кому",
                placeholder = "Выберите кому",
                options = RECIPIENTS,
                selected = state.recipient,
                onSelected = { v -> viewModel.update { copy(recipient = v) } }
            )
            MatuleTextField(
                value = state.source,
                onValueChange = { v -> viewModel.update { copy(source = v) } },
                label = "Источник описания",
                placeholder = "example.com"
            )
            MatuleSelect(
                label = "Категория",
                placeholder = "Выберите категорию",
                options = state.categories.map { it.name },
                selected = state.categoryName,
                onSelected = { v -> viewModel.update { copy(categoryName = v) } }
            )
            MatulePhotoBox(
                photo = preview,
                onClick = { showPhotoSheet = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            MatuleButton(
                text = if (state.isLoading) "Отправка..." else "Подтвердить",
                onClick = viewModel::onSubmit,
                enabled = state.canSubmit
            )
        }
    }

    if (showPhotoSheet) {
        MatuleBottomSheet(onDismiss = { showPhotoSheet = false }, title = "Загрузить фото") {
            Text(
                text = "📷  Сделать фото",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showPhotoSheet = false
                        cameraLauncher.launch(null)
                    }
                    .padding(vertical = 14.dp)
            )
            Text(
                text = "🖼  Выбрать из галереи",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showPhotoSheet = false
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .padding(vertical = 14.dp)
            )
        }
    }

    state.errorMessage?.let { message ->
        MatuleErrorDialog(message = message, onDismiss = viewModel::onErrorDismissed)
    }
}