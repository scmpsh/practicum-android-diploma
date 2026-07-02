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
    private var rootCountries: List<FilterArea> = emptyList()
    private var selectedCountryId: Int? = null

    init {
        selectedCountryId = filterInteractor.getFilterSettings().countryId
        loadRegions()
    }

    private fun loadRegions() {
        viewModelScope.launch {
            areaInteractor.getAreas().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val countries = result.data ?: emptyList()
                        rootCountries = countries
                        allRegions = extractRegions(countries, selectedCountryId).sortedBy { it.name }

                        _state.value = if (allRegions.isEmpty()) {
                            RegionsState.Empty
                        } else {
                            RegionsState.Content(allRegions)
                        }
                    }

                    is Resource.Error -> {
                        _state.value = RegionsState.Error(result.message ?: "Ошибка")
                    }
                }
            }
        }
    }

    private fun extractRegions(countries: List<FilterArea>, countryId: Int?): List<FilterArea> {
        return if (countryId == null) {
            flatten(countries).filter { it.parentId != null }
        } else {
            val country = countries.firstOrNull { it.id == countryId }
            if (country != null) {
                flatten(country.areas).filter { it.parentId != null }
            } else {
                emptyList()
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text

        if (allRegions.isEmpty()) return

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

        // Find which country root contains this region
        val parentCountry = rootCountries.firstOrNull { country ->
            country.id == area.id || isDescendant(country, area.id)
        }

        filterInteractor.saveFilterSettings(
            settings.copy(
                regionId = area.id,
                regionName = area.name,
                countryId = parentCountry?.id ?: settings.countryId,
                countryName = parentCountry?.name ?: settings.countryName
            )
        )
    }

    private fun isDescendant(parent: FilterArea, targetId: Int): Boolean {
        if (parent.areas.any { it.id == targetId }) return true
        return parent.areas.any { isDescendant(it, targetId) }
    }

    private fun flatten(areas: List<FilterArea>): List<FilterArea> =
        areas.flatMap { area ->
            listOf(area) + flatten(area.areas)
        }
}
