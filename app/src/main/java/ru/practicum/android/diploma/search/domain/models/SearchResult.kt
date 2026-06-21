package ru.practicum.android.diploma.search.domain.models

data class SearchResult(
    val vacancies: List<Vacancy>,
    val found: Int,
    val pages: Int
)
