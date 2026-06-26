package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.FilterArea
import ru.practicum.android.diploma.search.domain.models.Resource

interface AreaInteractor {
    fun getAreas(): Flow<Resource<List<FilterArea>>>
}
