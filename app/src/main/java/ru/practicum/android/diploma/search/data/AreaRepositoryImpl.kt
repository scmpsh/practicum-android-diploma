package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.search.data.dto.AreasRequest
import ru.practicum.android.diploma.search.data.dto.FilterAreaDto
import ru.practicum.android.diploma.search.data.mappers.FilterAreaMapper
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.domain.api.AreaRepository
import ru.practicum.android.diploma.search.domain.models.FilterArea
import ru.practicum.android.diploma.search.domain.models.Resource

class AreaRepositoryImpl(
    private val networkClient: NetworkClient
) : AreaRepository {

    override fun getAreas(): Flow<Resource<List<FilterArea>>> = flow {
        val response = networkClient.doRequest(AreasRequest())

        when (response.resultCode) {
            RetrofitNetworkClient.NO_CONNECTION -> {
                emit(Resource.Error("Нет интернет соединения"))
            }

            RetrofitNetworkClient.HTTP_OK -> {
                val dto = response.data as? List<FilterAreaDto>

                if (dto == null) {
                    emit(Resource.Error("Ошибка обработки данных"))
                } else {
                    emit(
                        Resource.Success(
                            dto.map(FilterAreaMapper::map)
                        )
                    )
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}
