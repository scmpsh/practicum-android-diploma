package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.search.domain.models.FilterArea

sealed class CountriesState {
    object Loading : CountriesState()
    data class Content(val data: List<FilterArea>) : CountriesState()
    data class Error(val message: String) : CountriesState()
}
