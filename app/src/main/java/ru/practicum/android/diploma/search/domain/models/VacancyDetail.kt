package ru.practicum.android.diploma.search.domain.models

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class VacancyDetail(
    val id: String,
    val name: String,
    val description: String,
    val salary: Salary?,
    val address: Address?,
    val experience: Experience?,
    val schedule: Schedule?,
    val employment: Employment?,
    val contacts: Contacts?,
    val employer: Employer,
    val area: Area,
    val skills: ImmutableList<String>,
    val url: String,
    val industry: Industry
)

@Immutable
data class Salary(
    val from: Int?,
    val to: Int?,
    val currency: String?
)

@Immutable
data class Address(
    val city: String,
    val street: String,
    val building: String,
    val raw: String
)

@Immutable
data class Experience(
    val id: String,
    val name: String
)

@Immutable
data class Schedule(
    val id: String,
    val name: String
)

@Immutable
data class Employment(
    val id: String,
    val name: String
)

@Immutable
data class Contacts(
    val id: String,
    val name: String,
    val email: String,
    val phones: ImmutableList<Phone>
)

@Immutable
data class Phone(
    val comment: String?,
    val formatted: String
)

@Immutable
data class Employer(
    val id: String,
    val name: String,
    val logo: String
)

@Immutable
data class Area(
    val id: Int,
    val name: String
)

@Immutable
data class Industry(
    val id: Int,
    val name: String
)
