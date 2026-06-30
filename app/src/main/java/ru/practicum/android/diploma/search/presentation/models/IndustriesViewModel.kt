package ru.practicum.android.diploma.search.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.FilterInteractor
import ru.practicum.android.diploma.search.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.search.domain.models.IndustryFilter
import ru.practicum.android.diploma.search.domain.models.Resource

class IndustriesViewModel(
    private val industriesInteractor: IndustriesInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<IndustriesState>(IndustriesState.Initial)
    val state: StateFlow<IndustriesState> = _state

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _selectedIndustry = MutableStateFlow(filterInteractor.getFilterSettings().industry)
    val selectedIndustry: StateFlow<String?> = _selectedIndustry

    private var allIndustries: List<IndustryFilter> = emptyList()

    init {
        loadIndustries()
    }

    fun loadIndustries() {
        viewModelScope.launch {
            _state.value = IndustriesState.Loading

            when (val result = industriesInteractor.getIndustries().first()) {
                is Resource.Success -> {
                    allIndustries = result.data.orEmpty()
                    applySearchFilter()
                }

                is Resource.Error -> {
                    _state.value = if (
                        result.message?.contains(NO_INTERNET_KEYWORD, ignoreCase = true) == true
                    ) {
                        IndustriesState.NoInternet
                    } else {
                        IndustriesState.Error
                    }
                }
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
        applySearchFilter()
    }

    fun onIndustryClick(industry: IndustryFilter) {
        _selectedIndustry.value = industry.name
    }

    fun onIndustrySelected(industry: IndustryFilter) {
        _selectedIndustry.value = industry.name
        val settings = filterInteractor.getFilterSettings()
        filterInteractor.saveFilterSettings(
            settings.copy(
                industry = industry.name
            )
        )
    }

    private fun applySearchFilter() {
        val query = _searchText.value
        val filteredIndustries = if (query.isBlank()) {
            allIndustries
        } else {
            allIndustries.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _state.value = if (filteredIndustries.isEmpty()) {
            IndustriesState.Empty
        } else {
            IndustriesState.Content(filteredIndustries)
        }
    }

    companion object {
        private const val NO_INTERNET_KEYWORD = "интернет"
    }
}
