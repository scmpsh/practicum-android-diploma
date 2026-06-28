package ru.practicum.android.diploma.search.data

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.domain.models.Vacancy

class VacancyMapper {

    fun map(dto: VacancyDto): Vacancy {
        return Vacancy(
            id = dto.id,
            name = dto.name,
            company = dto.company,
            city = dto.city,
            salaryFrom = dto.salary?.from,
            salaryTo = dto.salary?.to,
            salaryCurrency = dto.salary?.currency,
            logoUrl = dto.logo
        )
    }
}
