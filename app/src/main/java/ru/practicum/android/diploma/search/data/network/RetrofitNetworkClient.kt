package ru.practicum.android.diploma.search.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancyDetailRequest

class RetrofitNetworkClient(
    private val apiService: HeadHunterApi,
    private val connectionChecker: ConnectionChecker
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!connectionChecker.isConnected()) {
            return Response().apply { resultCode = NO_CONNECTION }
        }

        return when (dto) {
            is VacanciesSearchRequest -> {
                searchVacancies(dto)
            }

            is VacancyDetailRequest -> {
                getVacancyDetail(dto)
            }

            else -> {
                Response().apply { resultCode = HTTP_BAD_REQUEST }
            }
        }
    }

    private suspend fun getVacancyDetail(
        dto: VacancyDetailRequest
    ): Response {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getVacancy(
                    token = "Bearer ${BuildConfig.API_ACCESS_TOKEN}",
                    id = dto.vacancyId
                ).apply {
                    resultCode = HTTP_OK
                }
            } catch (e: Exception) {
                Log.e("Network", "Request failed", e)
                Response().apply { resultCode = INNER_SERVER_ERROR }
            }
        }
    }

    private suspend fun searchVacancies(
        dto: VacanciesSearchRequest
    ): Response {
        val options: HashMap<String, String> = HashMap()
        options["text"] = dto.expression

        return withContext(Dispatchers.IO) {
            try {
                apiService.searchVacancies(
                    token = "Bearer ${BuildConfig.API_ACCESS_TOKEN}",
                    options = options
                ).apply {
                    resultCode = HTTP_OK
                }
            } catch (e: Exception) {
                Log.e("Network", "Request failed", e)
                Response().apply { resultCode = INNER_SERVER_ERROR }
            }
        }
    }

    companion object {
        const val HTTP_OK = 200
        const val HTTP_BAD_REQUEST = 400
        const val NO_CONNECTION = -1
        const val INNER_SERVER_ERROR = 500
    }
}
