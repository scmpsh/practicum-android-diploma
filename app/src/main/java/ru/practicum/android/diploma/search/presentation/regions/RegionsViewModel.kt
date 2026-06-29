package ru.practicum.android.diploma.search.presentation.regions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.AreaInteractor
import ru.practicum.android.diploma.search.domain.api.FilterInteractor
import ru.practicum.android.diploma.search.domain.models.FilterArea
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.presentation.models.RegionsState

class RegionsViewModel(
    private val areaInteractor: AreaInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private val _state = MutableStateFlow<RegionsState>(RegionsState.Loading)
    val state: StateFlow<RegionsState> = _state

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText
    private var allRegions: List<FilterArea> = emptyList()

    init {
        loadRegions()
    }

    private fun loadRegions() {
        val countryId = filterInteractor.getFilterSettings().countryId

        viewModelScope.launch {
            areaInteractor.getAreas().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        allRegions = if (countryId != null) {
                            val country = result.data?.firstOrNull { it.id == countryId }
                            country?.areas.orEmpty()
                        } else {
                            val allAreas = flatten(result.data.orEmpty())
                            allAreas.filter { it.parentId != null }
                        }

                        _state.value = RegionsState.Content(allRegions)
                    }

                    is Resource.Error -> {
                        _state.value =
                            RegionsState.Error(result.message ?: "Ошибка")
                    }
                }
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text

        val filtered = if (text.isBlank()) {
            allRegions
        } else {
            allRegions.filter {
                it.name.contains(text, ignoreCase = true)
            }
        }

        _state.value = if (filtered.isEmpty()) {
            RegionsState.Empty
        } else {
            RegionsState.Content(filtered)
        }
    }

    fun onRegionClick(area: FilterArea) {
        val settings = filterInteractor.getFilterSettings()
        filterInteractor.saveFilterSettings(
            settings.copy(
                regionId = area.id
            )
        )
    }

    private fun flatten(areas: List<FilterArea>): List<FilterArea> =
        areas.flatMap { area ->
            listOf(area) + flatten(area.areas)
        }
}
