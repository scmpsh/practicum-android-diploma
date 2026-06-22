package ru.practicum.android.diploma.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.search.domain.models.Resource
import ru.practicum.android.diploma.search.domain.models.Salary

class DetailsViewModel(
    private val interactor: VacancyDetailsInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val state: StateFlow<DetailsState> = _state

    fun loadVacancy(id: String) {
        viewModelScope.launch {
            _state.value = DetailsState.Loading

            when (val result = interactor.getVacancyDetails(id).first()) {

                is Resource.Success -> {
                    val data = result.data ?: run {
                        _state.value = DetailsState.Error
                        return@launch
                    }

                    _state.value = DetailsState.Content(
                        title = data.name,
                        salary = formatSalary(data.salary),
                        company = data.employer.name,
                        location = data.address?.raw ?: data.area.name,
                        logoUrl = data.employer.logo,
                        experience = data.experience?.name.orEmpty(),
                        schedule = data.schedule?.name.orEmpty(),
                        employment = data.employment?.name.orEmpty(),
                        descriptionHtml = data.description,
                        skills = data.skills ?: emptyList(),
                        contacts = data.contacts?.email
                    )
                }

                is Resource.Error -> {
                    _state.value = if (
                        result.message?.contains(NO_INTERNET_KEYWORD, ignoreCase = true) == true
                    ) {
                        DetailsState.NoInternet
                    } else {
                        DetailsState.Error
                    }
                }
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
