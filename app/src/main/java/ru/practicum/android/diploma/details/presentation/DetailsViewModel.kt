package ru.practicum.android.diploma.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.search.domain.models.Resource

class DetailsViewModel(
    private val interactor: VacancyDetailsInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val state: StateFlow<DetailsState> = _state

    fun loadVacancy(id: String) {
        viewModelScope.launch {
            _state.value = DetailsState.Loading

            when (val result = interactor.getVacancyDetails(id).first()) {

                is Resource.Success -> {
                    val data = result.data ?: run {
                        _state.value = DetailsState.Error
                        return@launch
                    }

                    _state.value = DetailsState.Content(
                        title = data.name,
                        company = data.employer.name,
                        location = data.address?.raw ?: data.area.name,
                        descriptionHtml = data.description,
                        skills = data.skills ?: emptyList(),
                        contacts = data.contacts?.email
                    )
                }

                is Resource.Error -> {
                    _state.value = DetailsState.Error
                }
            }
        }
    }
}
