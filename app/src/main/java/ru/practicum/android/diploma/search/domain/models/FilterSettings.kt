package ru.practicum.android.diploma.search.domain.models

data class FilterSettings(
    val placeOfWork: String? = null,
    val industry: String? = null,
    val salary: String = "",
    val doNotShowWithoutSalary: Boolean = false,
    val countryId: Int? = null,
    val regionId: Int? = null
)
