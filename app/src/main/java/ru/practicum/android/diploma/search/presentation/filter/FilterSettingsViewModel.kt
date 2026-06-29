package ru.practicum.android.diploma.search.presentation.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.search.domain.api.FilterInteractor
import ru.practicum.android.diploma.search.domain.models.FilterSettings

class FilterSettingsViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(filterInteractor.getFilterSettings().toState())
    val state: StateFlow<FilterSettingsState> = _state

    fun onSalaryChange(value: String) {
        updateState(
            _state.value.copy(
                salary = value.filter { it.isDigit() }
            )
        )
    }

    fun onSalaryClearClick() {
        updateState(
            _state.value.copy(
                salary = ""
            )
        )
    }

    fun onDoNotShowWithoutSalaryChange(value: Boolean) {
        updateState(
            _state.value.copy(
                doNotShowWithoutSalary = value
            )
        )
    }

    fun onPlaceOfWorkSelected(value: String) {
        updateState(
            _state.value.copy(
                placeOfWork = value
            )
        )
    }

    fun onIndustrySelected(value: String) {
        updateState(
            _state.value.copy(
                industry = value
            )
        )
    }

    fun onResetClick() {
        filterInteractor.clearFilterSettings()
        _state.value = FilterSettingsState()
    }

    private fun updateState(newState: FilterSettingsState) {
        _state.value = newState
        filterInteractor.saveFilterSettings(newState.toFilterSettings())
    }

    private fun FilterSettings.toState(): FilterSettingsState {
        return FilterSettingsState(
            placeOfWork = placeOfWork,
            industry = industry,
            salary = salary,
            doNotShowWithoutSalary = doNotShowWithoutSalary
        )
    }

    private fun FilterSettingsState.toFilterSettings(): FilterSettings {
        val currentSettings = filterInteractor.getFilterSettings()
        return FilterSettings(
            placeOfWork = placeOfWork,
            industry = industry,
            salary = salary,
            doNotShowWithoutSalary = doNotShowWithoutSalary,
            countryId = currentSettings.countryId,
            regionId = currentSettings.regionId
        )
    }
}
