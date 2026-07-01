package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.search.domain.api.IndustriesRepository
import ru.practicum.android.diploma.search.domain.models.IndustryFilter
import ru.practicum.android.diploma.search.domain.models.Resource

class IndustriesInteractorImpl(
    private val repository: IndustriesRepository
) : IndustriesInteractor {

    override fun getIndustries(): Flow<Resource<List<IndustryFilter>>> {
        return repository.getIndustries()
    }
}
