package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.search.domain.models.FilterArea

sealed class RegionsState {
    object Loading : RegionsState()
    data class Content(val data: List<FilterArea>) : RegionsState()
    data class Error(val message: String) : RegionsState()
    object Empty : RegionsState()

}
