package ru.practicum.android.diploma.details.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.search.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.SalaryFormatter

class DetailsViewModel(
    private val interactor: VacancyDetailsInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val state: StateFlow<DetailsState> = _state

    private var vacancyDetail: VacancyDetail? = null

    fun loadVacancy(id: String) {
        viewModelScope.launch {
            _state.value = DetailsState.Loading

            val isFavorite = favoritesInteractor.isFavorite(id)

            when (val result = interactor.getVacancyDetails(id).first()) {
                is Resource.Success -> {
                    val data = result.data ?: run {
                        handleError(id, isFavorite, null)
                        return@launch
                    }
                    vacancyDetail = data
                    _state.value = mapToContent(data, isFavorite)
                }

                is Resource.Error -> {
                    handleError(id, isFavorite, result.message)
                }
            }
        }
    }

    private suspend fun handleError(id: String, isFavorite: Boolean, message: String?) {
        if (isFavorite) {
            if (message == "404") {
                favoritesInteractor.removeFromFavorites(id)
                _state.value = DetailsState.Error
                return
            }
            try {
                val data = favoritesInteractor.getVacancyDetail(id)
                vacancyDetail = data
                _state.value = mapToContent(data, true)
                return
            } catch (e: Exception) {
                // fallback
            }
        }

        _state.value = if (message?.contains(NO_INTERNET_KEYWORD, ignoreCase = true) == true) {
            DetailsState.NoInternet
        } else {
            DetailsState.Error
        }
    }

    private fun mapToContent(data: VacancyDetail, isFavorite: Boolean): DetailsState.Content {
        return DetailsState.Content(
            title = data.name,
            salary = SalaryFormatter.format(
                context = context,
                from = data.salary?.from,
                to = data.salary?.to,
                currency = data.salary?.currency
            ),
            company = data.employer.name,
            location = data.address?.raw.orEmpty().ifBlank { data.area.name },
            logoUrl = data.employer.logo,
            experience = data.experience?.name.orEmpty(),
            schedule = data.schedule?.name.orEmpty(),
            employment = data.employment?.name.orEmpty(),
            descriptionHtml = data.description,
            skills = data.skills,
            contactName = data.contacts?.name,
            contactEmail = data.contacts?.email,
            contactPhones = data.contacts?.phones.orEmpty(),
            url = data.url,
            isFavorite = isFavorite,
        )
    }

    fun onFavoriteClick() {
        val currentState = _state.value
        if (currentState is DetailsState.Content) {
            val vacancy = vacancyDetail ?: return
            viewModelScope.launch {
                if (currentState.isFavorite) {
                    favoritesInteractor.removeFromFavorites(vacancy.id)
                } else {
                    favoritesInteractor.addToFavorites(vacancy)
                }
                _state.value = currentState.copy(isFavorite = !currentState.isFavorite)
            }
        }
    }

    companion object {
        private const val NO_INTERNET_KEYWORD = "интернет"
    }
}
