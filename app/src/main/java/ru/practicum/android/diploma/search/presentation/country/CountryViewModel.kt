package ru.practicum.android.diploma.search.presentation.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.data.filterstoragetemp.FilterStorage
import ru.practicum.android.diploma.search.domain.api.AreaInteractor
import ru.practicum.android.diploma.search.domain.models.FilterArea
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.presentation.models.CountriesState

class CountryViewModel(
    private val interactor: AreaInteractor,
    private val filterStorage: FilterStorage
) : ViewModel() {
    private val _state = MutableStateFlow<CountriesState>(CountriesState.Loading)
    val state: StateFlow<CountriesState> = _state

    fun loadCountries() {
        viewModelScope.launch {
            interactor.getAreas().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = CountriesState.Content(result.data ?: emptyList())
                    }

                    is Resource.Error -> {
                        _state.value = CountriesState.Error(result.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun onCountryClick(area: FilterArea) {
        filterStorage.saveCountryId(area.id)
    }
}
