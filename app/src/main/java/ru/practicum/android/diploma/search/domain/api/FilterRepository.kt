package ru.practicum.android.diploma.search.domain.api

import ru.practicum.android.diploma.search.domain.models.FilterSettings

interface FilterRepository {

    fun getFilterSettings(): FilterSettings

    fun saveFilterSettings(settings: FilterSettings)

    fun clearFilterSettings()
}
