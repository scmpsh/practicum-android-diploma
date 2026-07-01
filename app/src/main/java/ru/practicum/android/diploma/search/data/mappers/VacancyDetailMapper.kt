package ru.practicum.android.diploma.search.data.mappers

import kotlinx.collections.immutable.toPersistentList
import ru.practicum.android.diploma.search.data.dto.AddressDto
import ru.practicum.android.diploma.search.data.dto.ContactsDto
import ru.practicum.android.diploma.search.data.dto.SalaryDto
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
            salary = mapSalary(dto.salary),
            address = mapAddress(dto.address),
            experience = dto.experience?.let {
                Experience(it.id, it.name)
            },
            schedule = dto.schedule?.let {
                Schedule(it.id, it.name)
            },
            employment = dto.employment?.let {
                Employment(it.id, it.name)
            },
            contacts = mapContacts(dto.contacts),
            employer = Employer(
                id = dto.employer.id,
                name = dto.employer.name,
                logo = dto.employer.logo.orEmpty()
            ),
            area = Area(
                id = dto.area.id,
                name = dto.area.name
            ),
            skills = (dto.skills ?: emptyList()).toPersistentList(),
            url = dto.url.orEmpty(),
            industry = Industry(
                id = dto.industry.id,
                name = dto.industry.name
            )
        )
    }

    private fun mapSalary(dto: SalaryDto?): Salary? {
        return dto?.let {
            Salary(it.from, it.to, it.currency)
        }
    }

    private fun mapAddress(dto: AddressDto?): Address? {
        return dto?.let {
            Address(
                city = it.city.orEmpty(),
                street = it.street.orEmpty(),
                building = it.building.orEmpty(),
                raw = it.raw.orEmpty()
            )
        }
    }

    private fun mapContacts(dto: ContactsDto?): Contacts? {
        return dto?.let { contacts ->
            Contacts(
                id = contacts.id.orEmpty(),
                name = contacts.name.orEmpty(),
                email = contacts.email.orEmpty(),
                phones = contacts.phones.orEmpty().map {
                    Phone(it.comment, it.formatted.orEmpty())
                }.toPersistentList()
            )
        }
    }
}
