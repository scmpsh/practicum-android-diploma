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

    @Headers("HH-User-Agent: Practicum-Android-Diploma (nickitamigal@yandex.ru)")
    @GET("/areas")
    suspend fun getAreas(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): List<FilterAreaDto>

    @Headers("HH-User-Agent: Practicum-Android-Diploma (nickitamigal@yandex.ru)")
    @GET("/industries")
    suspend fun getIndustries(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): List<FilterIndustryDto>

    @Headers("HH-User-Agent: Practicum-Android-Diploma (nickitamigal@yandex.ru)")
    @GET("/vacancies")
    suspend fun searchVacancies(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json",
        @QueryMap options: Map<String, String>
    ): VacanciesSearchResponseDto

    @Headers("HH-User-Agent: Practicum-Android-Diploma (nickitamigal@yandex.ru)")
    @GET("/vacancies/{id}")
    suspend fun getVacancy(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Path("id") id: String
    ): VacancyDetailDto
}
