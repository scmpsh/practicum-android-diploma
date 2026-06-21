package ru.practicum.android.diploma.search.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.Resource

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state

    private var latestSearchText: String? = null
    private var searchJob: Job? = null
    private var pagingJob: Job? = null

    private var currentPage = 0
    private var maxPages = 0
    private var isNextPageLoading = false

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText

        if (changedText.isEmpty()) {
            _state.value = SearchState.Initial
            searchJob?.cancel()
            pagingJob?.cancel()
            isNextPageLoading = false
            return
        }

        searchJob?.cancel()
        pagingJob?.cancel()
        isNextPageLoading = false
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search(changedText)
        }
    }

    fun onLastItemReached() {
        if (currentPage < maxPages - 1 && !isNextPageLoading) {
            val currentState = _state.value
            if (currentState is SearchState.Content) {
                _state.value = currentState.copy(isPaging = true, toastMessage = null)
                pagingJob = viewModelScope.launch {
                    loadNextPage()
                }
            }
        }
    }

    fun onToastShown() {
        val currentState = _state.value
        if (currentState is SearchState.Content) {
            _state.value = currentState.copy(toastMessage = null)
        }
    }

    private suspend fun search(newSearchText: String) {
        _state.value = SearchState.Loading
        currentPage = 0

        searchInteractor.searchVacancies(newSearchText, currentPage).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val searchResult = resource.data!!
                    maxPages = searchResult.pages
                    if (searchResult.vacancies.isEmpty()) {
                        _state.value = SearchState.Empty
                    } else {
                        _state.value = SearchState.Content(searchResult.vacancies, searchResult.found)
                    }
                }

                is Resource.Error -> {
                    _state.value = SearchState.Error
                }
            }
        }
    }

    private suspend fun loadNextPage() {
        isNextPageLoading = true
        val nextPage = currentPage + 1

        searchInteractor.searchVacancies(latestSearchText ?: "", nextPage).collect { resource ->
            val currentState = _state.value
            if (currentState is SearchState.Content) {
                when (resource) {
                    is Resource.Success -> {
                        val searchResult = resource.data!!
                        currentPage = nextPage
                        maxPages = searchResult.pages
                        _state.value = currentState.copy(
                            vacancies = (currentState.vacancies + searchResult.vacancies).distinctBy { it.id },
                            isPaging = false
                        )
                    }

                    is Resource.Error -> {
                        _state.value = currentState.copy(
                            isPaging = false,
                            toastMessage = resource.message
                        )
                    }
                }
            }
            isNextPageLoading = false
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
