package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.SearchResult

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    override fun searchVacancies(expression: String, page: Int): Flow<Resource<SearchResult>> {
        return repository.searchVacancies(expression, page)
    }
}
