package ru.practicum.android.diploma.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.ui.models.SearchState

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state

    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText

        if (changedText.isEmpty()) {
            _state.value = SearchState.Initial
            searchJob?.cancel()
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search(changedText)
        }
    }

    private suspend fun search(newSearchText: String) {
        _state.value = SearchState.Loading

        searchInteractor.searchVacancies(newSearchText).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val (vacancies, found) = resource.data!!
                    if (vacancies.isEmpty()) {
                        _state.value = SearchState.Empty
                    } else {
                        _state.value = SearchState.Content(vacancies, found)
                    }
                }

                is Resource.Error -> {
                    _state.value = SearchState.Error(resource.message ?: "")
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
