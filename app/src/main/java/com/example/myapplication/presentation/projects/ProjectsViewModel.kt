package com.example.myapplication.presentation.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.Project
import com.example.myapplication.domain.repository.ReminderScheduler
import com.example.myapplication.domain.usecase.GetProjectsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProjectsUiState(
    val isLoading: Boolean = true,
    val projects: List<Project> = emptyList(),
    val error: String? = null
)

/** Логика экрана «Проекты». */
class ProjectsViewModel(
    private val getProjects: GetProjectsUseCase,
    private val reminders: ReminderScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectsUiState())
    val state: StateFlow<ProjectsUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = ProjectsUiState(isLoading = true)
            _state.value = when (val result = getProjects()) {
                is Resource.Success -> ProjectsUiState(isLoading = false, projects = result.data)
                is Resource.Failure -> ProjectsUiState(isLoading = false, error = result.message)
            }
        }
    }

    /** Пользователь открыл проект: (пере)запускаем 3-минутное напоминание. */
    fun onProjectOpened(project: Project) {
        reminders.scheduleProjectInactivity(project.title)
    }
}