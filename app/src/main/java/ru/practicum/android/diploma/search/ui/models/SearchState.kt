package ru.practicum.android.diploma.search.ui.models

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface SearchState {
    object Initial : SearchState
    object Loading : SearchState
    data class Content(val vacancies: List<Vacancy>, val found: Int) : SearchState
    data class Error(val message: String) : SearchState
    object Empty : SearchState
}
