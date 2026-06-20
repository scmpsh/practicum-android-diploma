
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
            description = entity.description ?: "",
            salary = mapSalary(entity),
            address = mapAddress(entity),
            experience = mapExperience(entity),
            schedule = mapSchedule(entity),
            employment = mapEmployment(entity),
            contacts = mapContacts(entity),
            employer = mapEmployer(entity),
            area = mapArea(entity),
            skills = parseSkills(entity.keySkills),
            url = entity.url ?: "",
            industry = Industry(
                id = 0,
                name = ""
            )
        )
    }

    // dividers
    private fun mapSalary(entity: VacancyEntity): Salary {
        return Salary(
            from = entity.salaryFrom,
            to = entity.salaryTo,
            currency = entity.salaryCurrency
        )
    }

    private fun mapAddress(entity: VacancyEntity): Address {
        return Address(
            city = entity.addressCity ?: "",
            street = entity.addressStreet ?: "",
            building = entity.addressBuilding ?: "",
            raw = entity.addressRaw ?: ""
        )
    }

    private fun mapExperience(entity: VacancyEntity): Experience {
        return Experience(
            id = entity.experience ?: "",
            name = entity.experience ?: ""
        )
    }

    private fun mapSchedule(entity: VacancyEntity): Schedule {
        return Schedule(
            id = entity.schedule ?: "",
            name = entity.schedule ?: ""
        )
    }

    private fun mapEmployment(entity: VacancyEntity): Employment {
        return Employment(
            id = entity.employment ?: "",
            name = entity.employment ?: ""
        )
    }

    private fun mapContacts(entity: VacancyEntity): Contacts {
        return Contacts(
            id = "",
            name = entity.contactsName ?: "",
            email = entity.contactsEmail ?: "",
            phones = parsePhones(entity.contactsPhones)
        )
    }

    private fun mapEmployer(entity: VacancyEntity): Employer {
        return Employer(
            id = "",
            name = entity.employerName ?: "",
            logo = entity.employerLogoUrl ?: ""
        )
    }

    private fun mapArea(entity: VacancyEntity): Area {
        return Area(
            id = 0,
            name = entity.areaName ?: ""
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
}
