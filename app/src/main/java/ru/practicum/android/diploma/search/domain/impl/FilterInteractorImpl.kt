package ru.practicum.android.diploma.search.domain.impl

import ru.practicum.android.diploma.search.domain.api.FilterInteractor
import ru.practicum.android.diploma.search.domain.api.FilterRepository
import ru.practicum.android.diploma.search.domain.models.FilterSettings

class FilterInteractorImpl(
    private val repository: FilterRepository
) : FilterInteractor {

    override fun getFilterSettings(): FilterSettings {
        return repository.getFilterSettings()
    }

    override fun saveFilterSettings(settings: FilterSettings) {
        repository.saveFilterSettings(settings)
    }

    override fun clearFilterSettings() {
        repository.clearFilterSettings()
    }
}
