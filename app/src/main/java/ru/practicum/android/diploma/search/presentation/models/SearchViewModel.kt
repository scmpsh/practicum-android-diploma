package ru.practicum.android.diploma.search.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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
        latestSearchText = changedText

        if (changedText.isBlank()) {
            _state.value = SearchState.Initial
            searchJob?.cancel()
            pagingJob?.cancel()
            return
        }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500)
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

        searchInteractor.searchVacancies(newSearchText, currentPage)
            .collect { resource ->
                when (resource) {

                    is Resource.Success -> {
                        val data = resource.data ?: run {
                            _state.value = SearchState.Error
                            return@collect
                        }

                        maxPages = data.pages

                        _state.value =
                            if (data.vacancies.isEmpty()) {
                                SearchState.Empty
                            } else {
                                SearchState.Content(
                                    vacancies = data.vacancies,
                                    found = data.found
                                )
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

        when (val resource =
            searchInteractor.searchVacancies(latestSearchText ?: "", nextPage).first()
        ) {
            is Resource.Success -> {
                val data = resource.data ?: return

                currentPage = nextPage
                maxPages = data.pages

                val currentState = _state.value
                if (currentState is SearchState.Content) {
                    _state.value = currentState.copy(
                        vacancies = (currentState.vacancies + data.vacancies)
                            .distinctBy { it.id },
                        isPaging = false
                    )
                }
            }

            is Resource.Error -> {
                val currentState = _state.value
                if (currentState is SearchState.Content) {
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
