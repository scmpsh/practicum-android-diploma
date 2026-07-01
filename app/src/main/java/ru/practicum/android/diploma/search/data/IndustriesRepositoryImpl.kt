package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.search.data.dto.IndustriesRequest
import ru.practicum.android.diploma.search.data.dto.IndustriesResponseDto
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.domain.api.IndustriesRepository
import ru.practicum.android.diploma.search.domain.models.IndustryFilter
import ru.practicum.android.diploma.search.domain.models.Resource

class IndustriesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: IndustryFilterMapper
) : IndustriesRepository {

    override fun getIndustries(): Flow<Resource<List<IndustryFilter>>> = flow {
        val response = networkClient.doRequest(IndustriesRequest)

        when (response.resultCode) {
            RetrofitNetworkClient.NO_CONNECTION -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            RetrofitNetworkClient.HTTP_OK -> {
                val data = (response as IndustriesResponseDto).industries.map { mapper.map(it) }
                emit(Resource.Success(data))
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}
