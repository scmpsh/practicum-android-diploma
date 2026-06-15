package ru.practicum.android.diploma.search.data.dto

data class VacanciesSearchResponseDto(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyDto>
) : Response()
