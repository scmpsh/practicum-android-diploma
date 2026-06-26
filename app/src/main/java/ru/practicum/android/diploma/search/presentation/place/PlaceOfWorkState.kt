package ru.practicum.android.diploma.search.presentation.place

data class PlaceOfWorkState(
    val country: String? = null,
    val region: String? = null
) {
    val hasSelectedPlace: Boolean
        get() = !country.isNullOrBlank() || !region.isNullOrBlank()

    val placeTitle: String
        get() = listOfNotNull(country, region)
            .filter { it.isNotBlank() }
            .joinToString(", ")
}
