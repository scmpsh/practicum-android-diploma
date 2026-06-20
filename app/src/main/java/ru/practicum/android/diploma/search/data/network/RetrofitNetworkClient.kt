package ru.practicum.android.diploma.search.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest

class RetrofitNetworkClient(
    private val apiService: HeadHunterApi,
    private val connectionChecker: ConnectionChecker
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        var result: Response

        if (!connectionChecker.isConnected()) {
            result = Response().apply { resultCode = NO_CONNECTION }
            return result
        }

        if (dto !is VacanciesSearchRequest) {
            result = Response().apply { resultCode = HTTP_BAD_REQUEST }
            return result
        }

        val options: HashMap<String, String> = HashMap()
        options["text"] = dto.expression

        result = withContext(Dispatchers.IO) {
            try {
                apiService.searchVacancies(
                    token = "Bearer ${BuildConfig.API_ACCESS_TOKEN}", options = options
                ).apply {
                    resultCode = HTTP_OK
                }
            } catch (e: Exception) {
                Log.e("Network", "Request failed", e)
                Response().apply { resultCode = INNER_SERVER_ERROR }
            }
        }

        return result
    }

    companion object {
        const val HTTP_OK = 200
        const val HTTP_BAD_REQUEST = 400
        const val NO_CONNECTION = -1
        const val INNER_SERVER_ERROR = 500
    }
}
