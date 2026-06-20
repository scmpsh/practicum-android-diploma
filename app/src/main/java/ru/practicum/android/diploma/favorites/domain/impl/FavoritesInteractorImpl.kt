package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoritesRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyDetail

class FavoritesInteractorImpl(
    private val repository: FavoritesRepository
) : FavoritesInteractor {

    override suspend fun addToFavorites(detail: VacancyDetail) {
        repository.addToFavorites(detail)
    }

    override suspend fun removeFromFavorites(id: String) {
        repository.removeFromFavorites(id)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return repository.isFavorite(id)
    }

    override fun getFavorites(): Flow<List<Vacancy>> {
        return repository.getFavorites()
    }

    override suspend fun getVacancyDetail(id: String): VacancyDetail {
        return repository.getVacancyDetail(id)
    }
}
