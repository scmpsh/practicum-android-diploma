package ru.practicum.android.diploma.search.data

import ru.practicum.android.diploma.search.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.search.domain.models.Address
import ru.practicum.android.diploma.search.domain.models.Area
import ru.practicum.android.diploma.search.domain.models.Contacts
import ru.practicum.android.diploma.search.domain.models.Employer
import ru.practicum.android.diploma.search.domain.models.Employment
import ru.practicum.android.diploma.search.domain.models.Experience
import ru.practicum.android.diploma.search.domain.models.Industry
import ru.practicum.android.diploma.search.domain.models.Phone
import ru.practicum.android.diploma.search.domain.models.Salary
import ru.practicum.android.diploma.search.domain.models.Schedule
import ru.practicum.android.diploma.search.domain.models.VacancyDetail

class VacancyDetailMapper {

    fun map(dto: VacancyDetailDto): VacancyDetail {
        return VacancyDetail(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            salary = dto.salary?.let {
                Salary(it.from, it.to, it.currency)
            },
            address = dto.address?.let {
                Address(
                    city = it.city.orEmpty(),
                    street = it.street.orEmpty(),
                    building = it.building.orEmpty(),
                    raw = it.raw.orEmpty()
                )
            },
            experience = dto.experience?.let {
                Experience(it.id, it.name)
            },
            schedule = dto.schedule?.let {
                Schedule(it.id, it.name)
            },
            employment = dto.employment?.let {
                Employment(it.id, it.name)
            },
            contacts = dto.contacts?.let { contacts ->
                Contacts(
                    id = contacts.id.orEmpty(),
                    name = contacts.name.orEmpty(),
                    email = contacts.email.orEmpty(),
                    phones = contacts.phones.orEmpty().map {
                        Phone(it.comment, it.formatted.orEmpty())
                    }
                )
            },
            employer = Employer(
                id = dto.employer.id,
                name = dto.employer.name,
                logo = dto.employer.logo.orEmpty()
            ),
            area = Area(
                id = dto.area.id,
                name = dto.area.name
            ),
            skills = dto.skills ?: emptyList(),
            url = dto.url.orEmpty(),
            industry = Industry(
                id = dto.industry.id,
                name = dto.industry.name
            )
        )
    }
}
