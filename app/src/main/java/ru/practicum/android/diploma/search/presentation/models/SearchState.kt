package ru.practicum.android.diploma.search.presentation.models

import kotlinx.collections.immutable.ImmutableList
import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface SearchState {
    val toastMessage: String?
        get() = null

    object Initial : SearchState
    object Loading : SearchState

    data class Content(
        val vacancies: ImmutableList<Vacancy>,
        val found: Int,
        val isPaging: Boolean = false,
        override val toastMessage: String? = null
    ) : SearchState

    object Error : SearchState
    object Empty : SearchState
    object NoInternet : SearchState
}
