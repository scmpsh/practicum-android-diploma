package ru.practicum.android.diploma.search.presentation.place

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.search.domain.api.FilterInteractor

class PlaceOfWorkViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(PlaceOfWorkState())
    val state: StateFlow<PlaceOfWorkState> = _state

    init {
        loadSettings()
    }

    fun loadSettings() {
        val settings = filterInteractor.getFilterSettings()
        _state.value = PlaceOfWorkState(
            country = settings.countryName,
            region = settings.regionName
        )
    }

    fun onCountryClearClick() {
        val settings = filterInteractor.getFilterSettings()
        filterInteractor.saveFilterSettings(
            settings.copy(
                countryId = null,
                countryName = null,
                regionId = null,
                regionName = null
            )
        )
        loadSettings()
    }

    fun onRegionClearClick() {
        val settings = filterInteractor.getFilterSettings()
        filterInteractor.saveFilterSettings(
            settings.copy(
                regionId = null,
                regionName = null
            )
        )
        loadSettings()
    }
}
