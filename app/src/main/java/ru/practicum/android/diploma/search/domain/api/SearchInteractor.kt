package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.FilterSettings
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.SearchResult

interface SearchInteractor {
    fun searchVacancies(
        expression: String,
        page: Int = 0,
        filterSettings: FilterSettings = FilterSettings()
    ): Flow<Resource<SearchResult>>
}
