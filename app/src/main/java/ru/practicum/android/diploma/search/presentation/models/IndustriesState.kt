package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.search.domain.models.IndustryFilter

sealed interface IndustriesState {

    object Initial : IndustriesState

    object Loading : IndustriesState

    data class Content(
        val industries: List<IndustryFilter>
    ) : IndustriesState

    object Empty : IndustriesState

    object NoInternet : IndustriesState

    object Error : IndustriesState
}
