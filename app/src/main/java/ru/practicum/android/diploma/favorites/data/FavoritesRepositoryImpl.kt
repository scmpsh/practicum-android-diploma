package ru.practicum.android.diploma.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.mapper.VacancyMapper
import ru.practicum.android.diploma.favorites.data.db.dao.VacancyDao
import ru.practicum.android.diploma.favorites.domain.api.FavoritesRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyDetail

class FavoritesRepositoryImpl(
    private val dao: VacancyDao
) : FavoritesRepository {

    override suspend fun addToFavorites(detail: VacancyDetail) {
        dao.insertVacancy(VacancyMapper.fromDetail(detail))
    }

    override suspend fun removeFromFavorites(id: String) {
        dao.deleteVacancyById(id)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return dao.isFavorite(id)
    }

    override fun getFavorites(): Flow<List<Vacancy>> {
        return dao.getAllVacancies()
            .map { list -> list.map { VacancyMapper.toVacancy(it) } }
    }

    override suspend fun getVacancyDetail(id: String): VacancyDetail {
        return VacancyMapper.fromEntity(dao.getVacancyById(id))
    }
}
