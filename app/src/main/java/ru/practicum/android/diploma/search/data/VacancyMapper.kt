package ru.practicum.android.diploma.search.data

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.data.dto.VacancySalaryDto
import ru.practicum.android.diploma.search.domain.models.Vacancy

class VacancyMapper {

    fun map(dto: VacancyDto): Vacancy {
        return Vacancy(
            id = dto.id,
            name = dto.name,
            company = dto.company,
            city = dto.city,
            salary = formatSalary(dto.salary),
            logoUrl = dto.logo
        )
    }

    private fun formatSalary(salaryDto: VacancySalaryDto?): String? {
        if (salaryDto == null) return null
        val from = salaryDto.from
        val to = salaryDto.to
        val currency = salaryDto.currency ?: ""

        return when {
            from != null && to != null -> "от $from до $to $currency"
            from != null -> "от $from $currency"
            to != null -> "до $to $currency"
            else -> null
        }
    }
}
