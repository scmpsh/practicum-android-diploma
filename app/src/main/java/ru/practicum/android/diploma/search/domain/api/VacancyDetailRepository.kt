package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.VacancyDetail

interface VacancyDetailRepository {
    fun getVacancyDetail(id: String): Flow<Resource<VacancyDetail>>
}
