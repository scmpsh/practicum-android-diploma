package ru.practicum.android.diploma.search.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.search.domain.models.Resource

class IndustriesViewModel(
    private val interactor: IndustriesInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<IndustriesState>(IndustriesState.Initial)
    val state: StateFlow<IndustriesState> = _state

    fun loadIndustries() {
        viewModelScope.launch {
            _state.value = IndustriesState.Loading

            when (val result = interactor.getIndustries().first()) {
                is Resource.Success -> {
                    val data = result.data.orEmpty()
                    _state.value = if (data.isEmpty()) {
                        IndustriesState.Empty
                    } else {
                        IndustriesState.Content(data)
                    }
                }

                is Resource.Error -> {
                    _state.value = if (
                        result.message?.contains(NO_INTERNET_KEYWORD, ignoreCase = true) == true
                    ) {
                        IndustriesState.NoInternet
                    } else {
                        IndustriesState.Error
                    }
                }
            }
        }
    }

    companion object {
        private const val NO_INTERNET_KEYWORD = "интернет"
    }
}
