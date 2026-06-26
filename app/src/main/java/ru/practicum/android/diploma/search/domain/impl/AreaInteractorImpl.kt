package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.api.AreaInteractor
import ru.practicum.android.diploma.search.domain.api.AreaRepository
import ru.practicum.android.diploma.search.domain.models.FilterArea
import ru.practicum.android.diploma.search.domain.models.Resource

class AreaInteractorImpl(
    private val repository: AreaRepository
) : AreaInteractor {

    override fun getAreas(): Flow<Resource<List<FilterArea>>> {
        return repository.getAreas()
    }
}
