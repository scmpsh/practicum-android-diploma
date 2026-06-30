package ru.practicum.android.diploma.search.data.dto

data class VacanciesSearchRequest(
    val expression: String,
    val page: Int = 0,
    val salary: String = "",
    val onlyWithSalary: Boolean = false
)
