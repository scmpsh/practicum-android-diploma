package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.search.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.search.data.dto.VacancyDetailRequest
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.domain.api.VacancyDetailRepository
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.VacancyDetail

class VacancyDetailRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: VacancyDetailMapper
) : VacancyDetailRepository {

    override fun getVacancyDetail(id: String): Flow<Resource<VacancyDetail>> = flow {
        val response = networkClient.doRequest(VacancyDetailRequest(id))

        when (response.resultCode) {
            RetrofitNetworkClient.NO_CONNECTION -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            RetrofitNetworkClient.HTTP_OK -> {
                val data = mapper.map(response as VacancyDetailDto)
                emit(Resource.Success(data))
            }

            404 -> {
                emit(Resource.Error("404"))
            }

            else -> {
                emit(Resource.Error("Произошла ошибка"))
            }
        }
    }
}
