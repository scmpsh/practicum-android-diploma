package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.search.data.dto.FilterAreaDto
import ru.practicum.android.diploma.search.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponseDto
import ru.practicum.android.diploma.search.data.dto.VacancyDetailDto

interface HeadHunterApi {

    @Headers(USER_AGENT)
    @GET("/areas")
    suspend fun getAreas(
        @Header(HEADER_AUTHORIZATION) token: String,
        @Header(HEADER_CONTENT_TYPE) contentType: String = CONTENT_TYPE_JSON
    ): List<FilterAreaDto>

    @Headers(USER_AGENT)
    @GET("/industries")
    suspend fun getIndustries(
        @Header(HEADER_AUTHORIZATION) token: String,
        @Header(HEADER_CONTENT_TYPE) contentType: String = CONTENT_TYPE_JSON
    ): List<FilterIndustryDto>

    @Headers(USER_AGENT)
    @GET("/vacancies")
    suspend fun searchVacancies(
        @Header(HEADER_AUTHORIZATION) token: String,
        @Header(HEADER_CONTENT_TYPE) contentType: String = CONTENT_TYPE_JSON,
        @QueryMap options: Map<String, String>
    ): VacanciesSearchResponseDto

    @Headers(USER_AGENT)
    @GET("/vacancies/{id}")
    suspend fun getVacancy(
        @Header(HEADER_AUTHORIZATION) token: String,
        @Header(HEADER_CONTENT_TYPE) contentType: String = CONTENT_TYPE_JSON,
        @Path("id") id: String
    ): VacancyDetailDto

    companion object {
        const val USER_AGENT = "HH-User-Agent: Practicum-Android-Diploma (nickitamigal@yandex.ru)"
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val CONTENT_TYPE_JSON = "application/json"
    }
}
