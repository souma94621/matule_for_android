package com.example.myapplication.presentation.createproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.model.NewProjectData
import com.example.myapplication.domain.model.PhotoData
import com.example.myapplication.domain.usecase.CreateProjectUseCase
import com.example.myapplication.domain.usecase.GetCategoriesUseCase
import com.example.myapplication.domain.util.parseDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Значения должны совпадать с вариантами Select `type` в Pocketbase (пока там одна заглушка). */
val PROJECT_TYPES = listOf("default")

/** Варианты поля «Кому» (поле text в Pocketbase, поэтому список можно менять свободно). */
val RECIPIENTS = listOf("Себе", "Другу", "Коллеге")

data class CreateProjectUiState(
    val type: String? = null,
    val title: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val recipient: String? = null,
    val source: String = "",
    val categoryName: String? = null,
    val photo: PhotoData? = null,
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val created: Boolean = false
) {
    val startError: String?
        get() = if (startDate.isNotEmpty() && parseDate(startDate) == null) "Формат дд.мм.гггг" else null

    val endError: String?
        get() = if (endDate.isNotEmpty() && parseDate(endDate) == null) "Формат дд.мм.гггг" else null

    /** Все поля и фото обязательны. */
    val canSubmit: Boolean
        get() = type != null && title.isNotBlank() && parseDate(startDate) != null &&
                parseDate(endDate) != null && recipient != null && source.isNotBlank() &&
                categoryName != null && photo != null && !isLoading
}

class CreateProjectViewModel(
    private val getCategories: GetCategoriesUseCase,
    private val createProject: CreateProjectUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateProjectUiState())
    val state: StateFlow<CreateProjectUiState> = _state.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val result = getCategories()
            if (result is Resource.Success) {
                _state.update { it.copy(categories = result.data) }
            }
        }
    }

    /** Меняет состояние формы: viewModel.update { copy(title = "...") }. */
    fun update(reducer: CreateProjectUiState.() -> CreateProjectUiState) {
        _state.update { it.reducer() }
    }

    fun onSubmit() {
        val s = _state.value
        val categoryId = s.categories.firstOrNull { it.name == s.categoryName }?.id.orEmpty()
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val data = NewProjectData(
                type = s.type.orEmpty(),
                title = s.title.trim(),
                startDate = s.startDate,
                endDate = s.endDate,
                recipient = s.recipient.orEmpty(),
                descriptionSource = s.source,
                categoryId = categoryId
            )
            when (val result = createProject(data, s.photo)) {
                is Resource.Success -> _state.update { it.copy(isLoading = false, created = true) }
                is Resource.Failure -> _state.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }

    fun onErrorDismissed() {
        _state.update { it.copy(errorMessage = null) }
    }

    /** Очищает форму после успешного создания (категории сохраняются). */
    fun reset() {
        _state.update { CreateProjectUiState(categories = it.categories) }
    }
}