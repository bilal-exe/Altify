package bilal.altify.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

/*
* represents dates where we have limited precision,
* such as only the year, or only the year and month
* */
data class DateWithPrecision(
    val year: Int,
    val month: Month?,
    val dayOfMonth: Int?,
)

fun DateWithPrecision.toLocalDate() =
    LocalDate(
        year = year,
        month = month ?: Month.JANUARY,
        dayOfMonth = dayOfMonth ?: 0
    )

fun LocalDate.toDateWithPrecision() =
    DateWithPrecision(
        year = year,
        month = month,
        dayOfMonth = dayOfMonth
    )
