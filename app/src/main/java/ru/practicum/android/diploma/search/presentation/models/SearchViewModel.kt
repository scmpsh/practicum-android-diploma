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

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

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

        searchJob?.cancel()

        if (changedText.isBlank()) {
            pagingJob?.cancel()
            isNextPageLoading = false
            currentPage = 0
            maxPages = 0
            _state.value = SearchState.Initial
            return
        }

        pagingJob?.cancel()
        isNextPageLoading = false

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search(changedText)
        }
    }

    fun onLastItemReached() {
        if (currentPage >= maxPages - 1 || isNextPageLoading) {
            return
        }

        val currentState = _state.value
        if (currentState !is SearchState.Content) {
            return
        }

        _state.value = currentState.copy(
            isPaging = true,
            toastMessage = null
        )

        pagingJob?.cancel()
        pagingJob = viewModelScope.launch {
            loadNextPage()
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

        when (val resource = searchInteractor.searchVacancies(newSearchText, currentPage).first()) {
            is Resource.Success -> {
                val searchResult = resource.data
                if (searchResult == null) {
                    _state.value = SearchState.Error
                    return
                }

                maxPages = searchResult.pages

                _state.value = if (searchResult.vacancies.isEmpty()) {
                    SearchState.Empty
                } else {
                    SearchState.Content(
                        vacancies = searchResult.vacancies,
                        found = searchResult.found,
                        isPaging = false,
                        toastMessage = null
                    )
                }
            }

            is Resource.Error -> {
                _state.value = SearchState.Error
            }
        }
    }

    private suspend fun loadNextPage() {
        isNextPageLoading = true
        val nextPage = currentPage + 1

        when (val resource = searchInteractor.searchVacancies(latestSearchText.orEmpty(), nextPage).first()) {
            is Resource.Success -> {
                val data = resource.data

                if (data == null) {
                    stopPagingWithError("Пустой ответ")
                    return
                }

                currentPage = nextPage
                maxPages = data.pages

                val currentState = _state.value
                if (currentState is SearchState.Content) {
                    _state.value = currentState.copy(
                        vacancies = (currentState.vacancies + data.vacancies)
                            .distinctBy { it.id },
                        isPaging = false,
                        toastMessage = null
                    )
                }
            }

            is Resource.Error -> {
                stopPagingWithError(resource.message)
            }
        }

        isNextPageLoading = false
    }

    private fun stopPagingWithError(message: String?) {
        val currentState = _state.value
        if (currentState is SearchState.Content) {
            _state.value = currentState.copy(
                isPaging = false,
                toastMessage = message ?: "Ошибка сервера"
            )
        }
        isNextPageLoading = false
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }
}
