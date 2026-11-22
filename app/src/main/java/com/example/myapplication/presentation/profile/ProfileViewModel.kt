package com.example.myapplication.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.common.Resource
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null
)

class ProfileViewModel(private val getProfile: GetProfileUseCase) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = ProfileUiState(isLoading = true)
            _state.value = when (val result = getProfile()) {
                is Resource.Success -> ProfileUiState(isLoading = false, user = result.data)
                is Resource.Failure -> ProfileUiState(isLoading = false, error = result.message)
            }
        }
    }
}