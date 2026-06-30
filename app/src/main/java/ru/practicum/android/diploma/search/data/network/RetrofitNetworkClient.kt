package ru.practicum.android.diploma.search.data.network

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.AreasRequest
import ru.practicum.android.diploma.search.data.dto.IndustriesRequest
import ru.practicum.android.diploma.search.data.dto.IndustriesResponseDto
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancyDetailRequest

class RetrofitNetworkClient(
    private val apiService: HeadHunterApi,
    private val connectionChecker: ConnectionChecker
) : NetworkClient {

    private val authHeader = "Bearer ${BuildConfig.API_ACCESS_TOKEN}"

    override suspend fun doRequest(dto: Any): Response {
        if (!connectionChecker.isConnected()) {
            return Response().apply { resultCode = NO_CONNECTION }
        }

        return when (dto) {
            is VacanciesSearchRequest -> searchVacancies(dto)
            is VacancyDetailRequest -> getVacancyDetail(dto)
            is IndustriesRequest -> getIndustries()
            is AreasRequest -> getAreas()
            else -> Response().apply { resultCode = HTTP_BAD_REQUEST }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun getVacancyDetail(dto: VacancyDetailRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getVacancy(
                    token = authHeader,
                    id = dto.vacancyId
                ).apply {
                    resultCode = HTTP_OK
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: retrofit2.HttpException) {
                Log.e(TAG, "HTTP error: ${e.code()}", e)
                Response().apply { resultCode = e.code() }
            } catch (e: Exception) {
                Log.e(TAG, MSG_REQUEST_FAILED, e)
                Response().apply { resultCode = INNER_SERVER_ERROR }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun getIndustries(): Response {
        return withContext(Dispatchers.IO) {
            try {
                IndustriesResponseDto(
                    industries = apiService.getIndustries(
                        token = authHeader
                    )
                ).apply {
                    resultCode = HTTP_OK
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: retrofit2.HttpException) {
                Log.e(TAG, "HTTP error: ${e.code()}", e)
                Response().apply { resultCode = e.code() }
            } catch (e: Exception) {
                Log.e(TAG, MSG_REQUEST_FAILED, e)
                Response().apply { resultCode = INNER_SERVER_ERROR }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun searchVacancies(dto: VacanciesSearchRequest): Response {
        val options = HashMap<String, String>()
        options[QUERY_TEXT] = dto.expression
        options[QUERY_PAGE] = dto.page.toString()

        if (dto.salary.isNotBlank()) {
            options[QUERY_SALARY] = dto.salary
        }

        if (dto.onlyWithSalary) {
            options[QUERY_ONLY_WITH_SALARY] = true.toString()
        }

        return withContext(Dispatchers.IO) {
            try {
                apiService.searchVacancies(
                    token = authHeader,
                    options = options
                ).apply {
                    resultCode = HTTP_OK
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: retrofit2.HttpException) {
                Log.e(TAG, "HTTP error: ${e.code()}", e)
                Response().apply { resultCode = e.code() }
            } catch (e: Exception) {
                Log.e(TAG, MSG_REQUEST_FAILED, e)
                Response().apply { resultCode = INNER_SERVER_ERROR }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun getAreas(): Response {
        return withContext(Dispatchers.IO) {
            try {
                val dto = apiService.getAreas(
                    token = authHeader
                )

                Response().apply {
                    resultCode = HTTP_OK
                    data = dto
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: retrofit2.HttpException) {
                Log.e(TAG, "HTTP error: ${e.code()}", e)
                Response().apply { resultCode = e.code() }
            } catch (e: Exception) {
                Log.e(TAG, MSG_REQUEST_FAILED, e)
                Response().apply { resultCode = INNER_SERVER_ERROR }
            }
        }
    }

    companion object {
        const val HTTP_OK = 200
        const val HTTP_BAD_REQUEST = 400
        const val NO_CONNECTION = -1
        const val INNER_SERVER_ERROR = 500

        private const val TAG = "Network"
        private const val MSG_REQUEST_FAILED = "Request failed"

        private const val QUERY_TEXT = "text"
        private const val QUERY_PAGE = "page"
        private const val QUERY_SALARY = "salary"
        private const val QUERY_ONLY_WITH_SALARY = "only_with_salary"
    }
}
