package ru.practicum.android.diploma.search.presentation.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.search.domain.api.FilterInteractor
import ru.practicum.android.diploma.search.domain.models.FilterSettings

class FilterSettingsViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(FilterSettingsState())
    val state: StateFlow<FilterSettingsState> = _state

    init {
        loadSettings()
    }

    fun loadSettings() {
        val settings = filterInteractor.getFilterSettings()
        val placeOfWorkText = when {
            settings.countryName != null && settings.regionName != null ->
                "${settings.countryName}, ${settings.regionName}"
            settings.countryName != null -> settings.countryName
            settings.regionName != null -> settings.regionName
            else -> null
        }

        _state.value = FilterSettingsState(
            placeOfWork = placeOfWorkText,
            industry = settings.industryName,
            salary = settings.salary?.toString() ?: "",
            doNotShowWithoutSalary = settings.onlyWithSalary ?: false
        )
    }

    fun onSalaryChange(value: String) {
        val cleanValue = value.filter { it.isDigit() }
        _state.value = _state.value.copy(
            salary = cleanValue
        )
        saveCurrentFilters()
    }

    fun onSalaryClearClick() {
        _state.value = _state.value.copy(
            salary = ""
        )
        saveCurrentFilters()
    }

    fun onDoNotShowWithoutSalaryChange(value: Boolean) {
        _state.value = _state.value.copy(
            doNotShowWithoutSalary = value
        )
        saveCurrentFilters()
    }

    private fun saveCurrentFilters() {
        val currentState = _state.value
        val currentSaved = filterInteractor.getFilterSettings()
        filterInteractor.saveFilterSettings(
            currentSaved.copy(
                salary = currentState.salary.toIntOrNull(),
                onlyWithSalary = currentState.doNotShowWithoutSalary
            )
        )
    }

    fun onPlaceOfWorkSelected() {
        loadSettings()
    }

    fun onIndustrySelected(value: String) {
        val currentSaved = filterInteractor.getFilterSettings()
        filterInteractor.saveFilterSettings(
            currentSaved.copy(industryName = value)
        )
        loadSettings()
    }

    fun onPlaceOfWorkClearClick() {
        val currentSaved = filterInteractor.getFilterSettings()
        filterInteractor.saveFilterSettings(
            currentSaved.copy(
                countryId = null,
                countryName = null,
                regionId = null,
                regionName = null
            )
        )
        loadSettings()
    }

    fun onIndustryClearClick() {
        val currentSaved = filterInteractor.getFilterSettings()
        filterInteractor.saveFilterSettings(
            currentSaved.copy(
                industryId = null,
                industryName = null
            )
        )
        loadSettings()
    }

    fun onResetClick() {
        filterInteractor.saveFilterSettings(FilterSettings())
        _state.value = FilterSettingsState()
    }
}
