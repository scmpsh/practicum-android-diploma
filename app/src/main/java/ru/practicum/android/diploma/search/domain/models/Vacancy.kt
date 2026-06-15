package ru.practicum.android.diploma.search.domain.models

data class Vacancy(
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: String?,
    val logoUrl: String?
)
