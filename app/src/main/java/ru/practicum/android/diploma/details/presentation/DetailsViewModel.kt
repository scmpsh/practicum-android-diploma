package ru.practicum.android.diploma.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.search.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.Salary
import ru.practicum.android.diploma.search.domain.models.VacancyDetail

class DetailsViewModel(
    private val interactor: VacancyDetailsInteractor,
    private val favoritesInteractor: FavoritesInteractor
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
                    val data = result.data
                    if (data != null) {
                        vacancyDetail = data
                        _state.value = mapToContent(data, isFavorite)
                    } else {
                        handleError(id, isFavorite, null)
                    }
                }

                is Resource.Error -> {
                    handleError(id, isFavorite, result.message)
                }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private suspend fun handleError(id: String, isFavorite: Boolean, message: String?) {
        if (isFavorite) {
            try {
                val data = favoritesInteractor.getVacancyDetail(id)
                vacancyDetail = data
                _state.value = mapToContent(data, true)
                return
            } catch (e: Exception) {
                // Если не удалось загрузить из БД, показываем ошибку
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
            salary = formatSalary(data.salary),
            company = data.employer.name,
            location = data.address?.raw ?: data.area.name,
            logoUrl = data.employer.logo,
            experience = data.experience?.name.orEmpty(),
            schedule = data.schedule?.name.orEmpty(),
            employment = data.employment?.name.orEmpty(),
            descriptionHtml = data.description,
            skills = data.skills,
            vacancyUrl = data.url,
            contactEmail = data.contacts?.email?.takeIf { it.isNotBlank() },
            contactPhone = data.contacts?.phones?.firstOrNull()?.formatted?.takeIf { it.isNotBlank() },
            contactComment = data.contacts?.phones?.firstOrNull()?.comment?.takeIf { !it.isNullOrBlank() },
            isFavorite = isFavorite
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

    private fun formatSalary(salary: Salary?): String {
        if (salary == null || salary.from == null && salary.to == null) {
            return "Уровень зарплаты не указан"
        }

        val currency = salary.currency.orEmpty()

        return when {
            salary.from != null && salary.to != null -> {
                "от ${salary.from} до ${salary.to} $currency"
            }

            salary.from != null -> {
                "от ${salary.from} $currency"
            }

            salary.to != null -> {
                "до ${salary.to} $currency"
            }

            else -> {
                "Уровень зарплаты не указан"
            }
        }
    }

    companion object {
        private const val NO_INTERNET_KEYWORD = "интернет"
    }
}
