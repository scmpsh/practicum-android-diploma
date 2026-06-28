package ru.practicum.android.diploma.search.data

import ru.practicum.android.diploma.search.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.search.domain.models.IndustryFilter

class IndustryFilterMapper {

    fun map(dto: FilterIndustryDto): IndustryFilter {
        return IndustryFilter(
            id = dto.id,
            name = dto.name
        )
    }
}
