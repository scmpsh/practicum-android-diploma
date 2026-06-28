package ru.practicum.android.diploma.favorites.data

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity
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
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyDetail

object VacancyMapper {

    fun fromDetail(detail: VacancyDetail): VacancyEntity {
        return VacancyEntity(
            id = detail.id,
            name = detail.name,
            description = detail.description,

            salaryFrom = detail.salary?.from,
            salaryTo = detail.salary?.to,
            salaryCurrency = detail.salary?.currency,

            employerName = detail.employer.name,
            employerLogoUrl = detail.employer.logo,

            areaName = detail.area.name,
            addressCity = detail.address?.city,
            addressStreet = detail.address?.street,
            addressBuilding = detail.address?.building,
            addressRaw = detail.address?.raw,

            experience = detail.experience?.name,
            schedule = detail.schedule?.name,
            employment = detail.employment?.name,

            contactsName = detail.contacts?.name,
            contactsEmail = detail.contacts?.email,
            contactsPhones = detail.contacts?.phones?.joinToString(PHONES_SEPARATOR) { it.formatted },
            contactsComments = detail.contacts?.phones?.firstOrNull()?.comment,

            keySkills = detail.skills.joinToString(SKILLS_SEPARATOR),
            url = detail.url,

            createdAt = System.currentTimeMillis()
        )
    }

    fun toVacancy(vacancyEntity: VacancyEntity): Vacancy {
        return Vacancy(
            id = vacancyEntity.id,
            name = vacancyEntity.name,
            company = vacancyEntity.employerName,
            city = vacancyEntity.areaName,
            salaryFrom = vacancyEntity.salaryFrom,
            salaryTo = vacancyEntity.salaryTo,
            salaryCurrency = vacancyEntity.salaryCurrency,
            logoUrl = vacancyEntity.employerLogoUrl
        )
    }

    fun fromEntity(entity: VacancyEntity): VacancyDetail {
        return VacancyDetail(
            id = entity.id,
            name = entity.name,
            description = entity.description.orEmpty(),
            salary = mapSalary(entity),
            address = mapAddress(entity),
            experience = mapExperience(entity),
            schedule = mapSchedule(entity),
            employment = mapEmployment(entity),
            contacts = mapContacts(entity),
            employer = mapEmployer(entity),
            area = mapArea(entity),
            skills = parseSkills(entity.keySkills),
            url = entity.url.orEmpty(),
            industry = Industry(
                id = DEFAULT_ID,
                name = EMPTY_VALUE
            )
        )
    }

    private fun mapSalary(entity: VacancyEntity): Salary {
        return Salary(
            from = entity.salaryFrom,
            to = entity.salaryTo,
            currency = entity.salaryCurrency
        )
    }

    private fun mapAddress(entity: VacancyEntity): Address {
        return Address(
            city = entity.addressCity.orEmpty(),
            street = entity.addressStreet.orEmpty(),
            building = entity.addressBuilding.orEmpty(),
            raw = entity.addressRaw.orEmpty()
        )
    }

    private fun mapExperience(entity: VacancyEntity): Experience {
        return Experience(
            id = entity.experience.orEmpty(),
            name = entity.experience.orEmpty()
        )
    }

    private fun mapSchedule(entity: VacancyEntity): Schedule {
        return Schedule(
            id = entity.schedule.orEmpty(),
            name = entity.schedule.orEmpty()
        )
    }

    private fun mapEmployment(entity: VacancyEntity): Employment {
        return Employment(
            id = entity.employment.orEmpty(),
            name = entity.employment.orEmpty()
        )
    }

    private fun mapContacts(entity: VacancyEntity): Contacts {
        return Contacts(
            id = EMPTY_VALUE,
            name = entity.contactsName.orEmpty(),
            email = entity.contactsEmail.orEmpty(),
            phones = parsePhones(entity.contactsPhones, entity.contactsComments)
        )
    }

    private fun mapEmployer(entity: VacancyEntity): Employer {
        return Employer(
            id = EMPTY_VALUE,
            name = entity.employerName.orEmpty(),
            logo = entity.employerLogoUrl.orEmpty()
        )
    }

    private fun mapArea(entity: VacancyEntity): Area {
        return Area(
            id = DEFAULT_ID,
            name = entity.areaName.orEmpty()
        )
    }

    private fun parseSkills(raw: String?): ImmutableList<String> {
        if (raw.isNullOrBlank()) return emptyList<String>().toPersistentList()

        return raw.split(SKILLS_SEPARATOR)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toPersistentList()
    }

    private fun parsePhones(rawPhones: String?, rawComment: String?): ImmutableList<Phone> {
        if (rawPhones.isNullOrBlank()) return emptyList<Phone>().toPersistentList()

        return rawPhones.split(PHONES_SEPARATOR)
            .mapIndexed { index, phone ->
                Phone(
                    comment = if (index == FIRST_ITEM_INDEX) rawComment else null,
                    formatted = phone.trim()
                )
            }
            .toPersistentList()
    }

    private const val SKILLS_SEPARATOR = ","
    private const val PHONES_SEPARATOR = ";"
    private const val EMPTY_VALUE = ""
    private const val DEFAULT_ID = 0
    private const val FIRST_ITEM_INDEX = 0
}
