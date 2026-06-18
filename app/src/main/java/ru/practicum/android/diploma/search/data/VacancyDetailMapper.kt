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
                Address(it.city, it.street, it.building, it.raw)
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
                    id = contacts.id,
                    name = contacts.name,
                    email = contacts.email,
                    phones = contacts.phones.map {
                        Phone(it.comment, it.formatted)
                    }
                )
            },
            employer = Employer(
                dto.employer.id,
                dto.employer.name,
                dto.employer.logo
            ),
            area = Area(
                dto.area.id,
                dto.area.name
            ),
            skills = dto.skills ?: emptyList(),
            url = dto.url,
            industry = Industry(
                dto.industry.id,
                dto.industry.name
            )
        )
    }
}
