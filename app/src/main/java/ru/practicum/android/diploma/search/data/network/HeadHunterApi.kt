package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.search.data.dto.FilterAreaDto
import ru.practicum.android.diploma.search.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.search.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchResponseDto

interface HeadHunterApi {

    @GET("/areas")
    suspend fun getAreas(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): List<FilterAreaDto>

    @GET("/industries")
    suspend fun getIndustries(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): List<FilterIndustryDto>

    @GET("/vacancies")
    suspend fun searchVacancies(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json",
        @QueryMap options: Map<String, String>
    ): VacanciesSearchResponseDto

    @GET("/vacancies/{id}")
    suspend fun getVacancy(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Path("id") id: String
    ): VacancyDetailDto
}
