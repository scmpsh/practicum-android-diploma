package ru.practicum.android.diploma.details.presentation

import ru.practicum.android.diploma.search.domain.models.Phone

sealed interface DetailsState {

    object Loading : DetailsState

    data class Content(
        val title: String,
        val salary: String,
        val company: String,
        val location: String,
        val logoUrl: String?,
        val experience: String,
        val schedule: String,
        val employment: String,
        val descriptionHtml: String,
        val skills: List<String>,
        val contactName: String?,
        val contactEmail: String?,
        val contactPhones: List<Phone>,
        val url: String,
        val isFavorite: Boolean
    ) : DetailsState

    object NoInternet : DetailsState

    object Error : DetailsState
}

