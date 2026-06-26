package ru.practicum.android.diploma.search.data.mappers

import kotlinx.collections.immutable.toPersistentList
import ru.practicum.android.diploma.search.data.dto.AddressDto
import ru.practicum.android.diploma.search.data.dto.ContactsDto
import ru.practicum.android.diploma.search.data.dto.EmployerDto
import ru.practicum.android.diploma.search.data.dto.EmploymentDto
import ru.practicum.android.diploma.search.data.dto.ExperienceDto
import ru.practicum.android.diploma.search.data.dto.FilterAreaDto
import ru.practicum.android.diploma.search.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.search.data.dto.SalaryDto
import ru.practicum.android.diploma.search.data.dto.ScheduleDto
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
            experience = mapExperience(dto.experience),
            schedule = mapSchedule(dto.schedule),
            employment = mapEmployment(dto.employment),
            contacts = mapContacts(dto.contacts),
            employer = mapEmployer(dto.employer),
            area = mapArea(dto.area),
            skills = dto.skills.orEmpty().toPersistentList(),
            url = dto.url.orEmpty(),
            industry = mapIndustry(dto.industry)
        )
    }

    private fun mapSalary(dto: SalaryDto?): Salary? = dto?.let {
        Salary(it.from, it.to, it.currency)
    }

    private fun mapAddress(dto: AddressDto?): Address? = dto?.let {
        Address(
            city = it.city.orEmpty(),
            street = it.street.orEmpty(),
            building = it.building.orEmpty(),
            raw = it.raw.orEmpty()
        )
    }

    private fun mapExperience(dto: ExperienceDto?): Experience? = dto?.let {
        Experience(it.id, it.name)
    }

    private fun mapSchedule(dto: ScheduleDto?): Schedule? = dto?.let {
        Schedule(it.id, it.name)
    }

    private fun mapEmployment(dto: EmploymentDto?): Employment? = dto?.let {
        Employment(it.id, it.name)
    }

    private fun mapContacts(dto: ContactsDto?): Contacts? = dto?.let { contacts ->
        Contacts(
            id = contacts.id.orEmpty(),
            name = contacts.name.orEmpty(),
            email = contacts.email.orEmpty(),
            phones = contacts.phones
                .orEmpty()
                .map {
                    Phone(it.comment, it.formatted.orEmpty())
                }
                .toPersistentList()
        )
    }

    private fun mapEmployer(dto: EmployerDto): Employer = Employer(
        id = dto.id,
        name = dto.name,
        logo = dto.logo.orEmpty()
    )

    private fun mapArea(dto: FilterAreaDto): Area = Area(
        id = dto.id,
        name = dto.name
    )

    private fun mapIndustry(dto: FilterIndustryDto): Industry = Industry(
        id = dto.id,
        name = dto.name
    )
}
