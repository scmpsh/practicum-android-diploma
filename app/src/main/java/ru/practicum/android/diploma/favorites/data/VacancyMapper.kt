package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.search.domain.models.*

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
            contactsPhones = detail.contacts?.phones?.joinToString(";") { it.formatted },
            contactsComments = null,

            keySkills = detail.skills.joinToString(","),
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
            salary = formatSalary(
                vacancyEntity.salaryFrom,
                vacancyEntity.salaryTo,
                vacancyEntity.salaryCurrency
            ),
            logoUrl = vacancyEntity.employerLogoUrl
        )
    }

    fun fromEntity(entity: VacancyEntity): VacancyDetail {
        return VacancyDetail(
            id = entity.id,
            name = entity.name,
            description = entity.description ?: "",

            salary = Salary(
                from = entity.salaryFrom,
                to = entity.salaryTo,
                currency = entity.salaryCurrency
            ),

            address = Address(
                city = entity.addressCity ?: "",
                street = entity.addressStreet ?: "",
                building = entity.addressBuilding ?: "",
                raw = entity.addressRaw ?: ""
            ),

            experience = Experience(
                id = entity.experience ?: "",
                name = entity.experience ?: ""
            ),

            schedule = Schedule(
                id = entity.schedule ?: "",
                name = entity.schedule ?: ""
            ),

            employment = Employment(
                id = entity.employment ?: "",
                name = entity.employment ?: ""
            ),

            contacts = Contacts(
                id = "",
                name = entity.contactsName ?: "",
                email = entity.contactsEmail ?: "",
                phones = parsePhones(entity.contactsPhones)
            ),

            employer = Employer(
                id = "",
                name = entity.employerName ?: "",
                logo = entity.employerLogoUrl ?: ""
            ),

            area = Area(
                id = 0,
                name = entity.areaName ?: ""
            ),

            skills = parseSkills(entity.keySkills),

            url = entity.url ?: "",

            industry = Industry(
                id = 0,
                name = ""
            )
        )
    }

    // helpers

    private fun parseSkills(raw: String?): List<String> {
        if (raw.isNullOrBlank()) return emptyList()
        return raw.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    private fun parsePhones(raw: String?): List<Phone> {
        if (raw.isNullOrBlank()) return emptyList()

        return raw.split(";")
            .map {
                Phone(
                    comment = null,
                    formatted = it.trim()
                )
            }
    }

    private fun formatSalary(from: Int?, to: Int?, currency: String?): String? {
        if (from == null && to == null) return null

        val cur = currency ?: ""

        return when {
            from != null && to != null -> "$from - $to $cur"
            from != null -> "от $from $cur"
            to != null -> "до $to $cur"
            else -> null
        }
    }
}
