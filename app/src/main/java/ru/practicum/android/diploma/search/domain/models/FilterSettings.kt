package ru.practicum.android.diploma.search.domain.models

data class FilterSettings(
    val countryId: Int? = null,
    val countryName: String? = null,
    val regionId: Int? = null,
    val regionName: String? = null,
    val industryId: String? = null,
    val industryName: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false
)
