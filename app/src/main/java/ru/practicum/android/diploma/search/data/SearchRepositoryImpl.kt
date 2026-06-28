package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponseDto
import ru.practicum.android.diploma.search.data.mappers.VacancyMapper
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.FilterSettings
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.SearchResult

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: VacancyMapper
) : SearchRepository {

    override fun searchVacancies(
        expression: String,
        page: Int,
        filterSettings: FilterSettings
    ): Flow<Resource<SearchResult>> = flow {
        val response = networkClient.doRequest(
            VacanciesSearchRequest(
                expression = expression,
                page = page,
                salary = filterSettings.salary?.toString().orEmpty(),
                onlyWithSalary = filterSettings.onlyWithSalary,
                area = (filterSettings.regionId ?: filterSettings.countryId)?.toString(),
                industry = filterSettings.industryId
            )
        )

        when (response.resultCode) {
            RetrofitNetworkClient.NO_CONNECTION -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            RetrofitNetworkClient.HTTP_OK -> {
                val searchResponse = response as VacanciesSearchResponseDto
                val vacancies = searchResponse.items.map { mapper.map(it) }
                emit(
                    Resource.Success(
                        SearchResult(
                            vacancies = vacancies,
                            found = searchResponse.found,
                            pages = searchResponse.pages
                        )
                    )
                )
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}
