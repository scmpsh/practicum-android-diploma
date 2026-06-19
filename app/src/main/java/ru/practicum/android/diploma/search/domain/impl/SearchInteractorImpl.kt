package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.Vacancy

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    override fun searchVacancies(expression: String): Flow<Resource<Pair<List<Vacancy>, Int>>> {
        return repository.searchVacancies(expression)
    }
}
