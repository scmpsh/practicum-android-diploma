package ru.practicum.android.diploma.details.presentation

sealed interface DetailsState {

    object Loading : DetailsState

    data class Content(

        val title: String,

        val company: String,

        val location: String,

        val descriptionHtml: String,

        val skills: List<String>,

        val contacts: String?

    ) : DetailsState

    object Error : DetailsState

}
