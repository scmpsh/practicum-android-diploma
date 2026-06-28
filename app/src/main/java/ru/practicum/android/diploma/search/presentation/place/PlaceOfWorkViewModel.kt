package ru.practicum.android.diploma.search.presentation.place

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlaceOfWorkViewModel : ViewModel() {

    private val _state = MutableStateFlow(PlaceOfWorkState())
    val state: StateFlow<PlaceOfWorkState> = _state

    fun onCountrySelected(country: String) {
        _state.value = _state.value.copy(
            country = country,
            region = null
        )
    }

    fun onCountryClearClick() {
        _state.value = _state.value.copy(
            country = null,
            region = null
        )
    }

    fun onRegionSelected(region: String) {
        _state.value = _state.value.copy(
            region = region
        )
    }
}
