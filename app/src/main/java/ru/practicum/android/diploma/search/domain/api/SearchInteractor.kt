package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface SearchInteractor {
    fun searchVacancies(expression: String): Flow<Pair<List<Vacancy>?, String?>>
}
