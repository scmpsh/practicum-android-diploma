package ru.practicum.android.diploma.search.presentation.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilterSettingsViewModel : ViewModel() {

    private val _state = MutableStateFlow(FilterSettingsState())
    val state: StateFlow<FilterSettingsState> = _state

    fun onSalaryChange(value: String) {
        _state.value = _state.value.copy(
            salary = value.filter { it.isDigit() }
        )
    }

    fun onSalaryClearClick() {
        _state.value = _state.value.copy(
            salary = ""
        )
    }

    fun onDoNotShowWithoutSalaryChange(value: Boolean) {
        _state.value = _state.value.copy(
            doNotShowWithoutSalary = value
        )
    }

    fun onPlaceOfWorkSelected(value: String) {
        _state.value = _state.value.copy(
            placeOfWork = value
        )
    }

    fun onIndustrySelected(value: String) {
        _state.value = _state.value.copy(
            industry = value
        )
    }

    fun onResetClick() {
        _state.value = FilterSettingsState()
    }
}
