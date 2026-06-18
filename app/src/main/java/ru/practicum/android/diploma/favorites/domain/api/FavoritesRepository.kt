package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyDetail

interface FavoritesRepository {

    suspend fun addToFavorites(detail: VacancyDetail)

    suspend fun removeFromFavorites(id: String)

    suspend fun isFavorite(id: String): Boolean

    fun getFavorites(): Flow<List<Vacancy>>

    suspend fun getVacancyDetail(id: String): VacancyDetail
}
