package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface SearchInteractor {
    fun searchVacancies(expression: String): Flow<Resource<Pair<List<Vacancy>, Int>>>
}
