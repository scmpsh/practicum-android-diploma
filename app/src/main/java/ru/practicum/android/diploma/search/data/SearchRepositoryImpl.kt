package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponseDto
import ru.practicum.android.diploma.search.data.mappers.VacancyMapper
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.SearchResult

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: VacancyMapper
) : SearchRepository {

    override fun searchVacancies(expression: String, page: Int): Flow<Resource<SearchResult>> = flow {
        val response = networkClient.doRequest(VacanciesSearchRequest(expression, page))
        when (response.resultCode) {
            RetrofitNetworkClient.NO_CONNECTION -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            RetrofitNetworkClient.HTTP_OK -> {
                val searchResponse = response as VacanciesSearchResponseDto
                val vacancies = searchResponse.items.map { mapper.map(it) }
                emit(Resource.Success(SearchResult(vacancies, searchResponse.found, searchResponse.pages)))
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}
