package ru.practicum.android.diploma.search.data.mappers

import ru.practicum.android.diploma.search.data.dto.FilterAreaDto
import ru.practicum.android.diploma.search.domain.models.FilterArea

object FilterAreaMapper {

    fun map(dto: FilterAreaDto): FilterArea =
        FilterArea(
            id = dto.id,
            name = dto.name,
            parentId = dto.parentId,
            areas = dto.areas.map(::map)
        )
}
