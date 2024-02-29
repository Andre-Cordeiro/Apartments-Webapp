package pt.unl.fct.di.project.presentation.dto

import java.time.LocalDate

data class PeriodCreateDTO (
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class PeriodInfoDTO(
    val apartId: Long,
    val date: LocalDate,
    val available: Boolean
)

data class AvailablePeriodInfoDTO(
    val apartId: Long,
    val date: LocalDate,
)
