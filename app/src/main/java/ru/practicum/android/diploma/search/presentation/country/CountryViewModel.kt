package ru.practicum.android.diploma.search.presentation.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.AreaInteractor
import ru.practicum.android.diploma.search.domain.api.FilterInteractor
import ru.practicum.android.diploma.search.domain.models.FilterArea
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.presentation.models.CountriesState

class CountryViewModel(
    private val interactor: AreaInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private val _state = MutableStateFlow<CountriesState>(CountriesState.Loading)
    val state: StateFlow<CountriesState> = _state

    fun loadCountries() {
        viewModelScope.launch {
            interactor.getAreas().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val countryNames = listOf(
                            "Россия",
                            "Украина",
                            "Казахстан",
                            "Азербайджан",
                            "Беларусь",
                            "Грузия",
                            "Кыргызстан",
                            "Узбекистан",
                            "Другие регионы"
                        )
                        val countries = result.data?.filter { it.parentId == null }
                            ?.filter { it.name in countryNames }
                            ?.sortedBy { countryNames.indexOf(it.name) } ?: emptyList()
                        _state.value = CountriesState.Content(countries)
                    }

                    is Resource.Error -> {
                        _state.value = CountriesState.Error(result.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun onCountryClick(area: FilterArea) {
        val settings = filterInteractor.getFilterSettings()
        val isDifferent = settings.countryId != area.id
        filterInteractor.saveFilterSettings(
            settings.copy(
                countryId = area.id,
                countryName = area.name,
                regionId = if (isDifferent) null else settings.regionId,
                regionName = if (isDifferent) null else settings.regionName
            )
        )
    }
}
