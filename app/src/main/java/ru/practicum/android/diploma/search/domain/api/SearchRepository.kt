package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.SearchResult

interface SearchRepository {
    fun searchVacancies(expression: String, page: Int = 0): Flow<Resource<SearchResult>>
}
