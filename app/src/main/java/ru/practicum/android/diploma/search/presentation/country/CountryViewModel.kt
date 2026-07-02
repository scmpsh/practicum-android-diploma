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

private const val OTHER_REGIONS = "Другие регионы"

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
                        val countries = result.data
                            ?.filter { it.parentId == null } // только страны
                            ?.sortedWith( // сортировка алфавит, в конце регионы
                                compareBy<FilterArea> { it.name == OTHER_REGIONS }
                                    .thenBy { it.name }
                            )
                            ?: emptyList()

                        _state.value = CountriesState.Content(countries)
                    }

                    is Resource.Error -> {
                        _state.value = CountriesState.Error(
                            result.message ?: "Unknown error"
                        )
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
