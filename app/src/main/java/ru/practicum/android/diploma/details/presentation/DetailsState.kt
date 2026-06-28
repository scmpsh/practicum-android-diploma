package ru.practicum.android.diploma.details.presentation

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
        val vacancyUrl: String,
        val contactEmail: String?,
        val contactPhone: String?,
        val contactComment: String?,
        val isFavorite: Boolean
    ) : DetailsState

    object NoInternet : DetailsState

    object Error : DetailsState
}
