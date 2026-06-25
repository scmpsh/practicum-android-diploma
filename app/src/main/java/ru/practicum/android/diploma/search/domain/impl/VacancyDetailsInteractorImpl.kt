package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.api.VacancyDetailRepository
import ru.practicum.android.diploma.search.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.VacancyDetail

class VacancyDetailsInteractorImpl(
    private val repository: VacancyDetailRepository
) : VacancyDetailsInteractor {

    override fun getVacancyDetails(id: String): Flow<Resource<VacancyDetail>> {
        return repository.getVacancyDetail(id)
    }
}
