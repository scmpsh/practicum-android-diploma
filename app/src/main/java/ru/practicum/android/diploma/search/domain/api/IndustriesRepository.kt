package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.IndustryFilter
import ru.practicum.android.diploma.search.domain.models.Resource

interface IndustriesRepository {

    fun getIndustries(): Flow<Resource<List<IndustryFilter>>>
}
