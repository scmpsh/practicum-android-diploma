package ru.practicum.android.diploma.search.data

import ru.practicum.android.diploma.search.domain.api.FilterRepository
import ru.practicum.android.diploma.search.domain.models.FilterSettings

class FilterRepositoryImpl(
    private val filterPreferences: FilterPreferences
) : FilterRepository {

    override fun getFilterSettings(): FilterSettings {
        return filterPreferences.getFilterSettings()
    }

    override fun saveFilterSettings(settings: FilterSettings) {
        filterPreferences.saveFilterSettings(settings)
    }

    override fun clearFilterSettings() {
        filterPreferences.clearFilterSettings()
    }
}
