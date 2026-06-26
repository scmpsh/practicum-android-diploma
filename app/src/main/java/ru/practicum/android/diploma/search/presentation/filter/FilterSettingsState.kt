package ru.practicum.android.diploma.search.presentation.filter

data class FilterSettingsState(
    val placeOfWork: String? = null,
    val industry: String? = null,
    val salary: String = "",
    val doNotShowWithoutSalary: Boolean = false
) {
    val hasAnyFilter: Boolean
        get() = !placeOfWork.isNullOrBlank() ||
            !industry.isNullOrBlank() ||
            salary.isNotBlank() ||
            doNotShowWithoutSalary
}
