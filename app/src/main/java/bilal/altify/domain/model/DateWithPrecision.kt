package bilal.altify.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month as KotlinMonth

/*
* represents dates where we have limited precision,
* such as only the year, or only the year and month
* */
sealed interface DateWithPrecision {
    data class Year(val year: Int) : DateWithPrecision
    data class Month(val year: Int, val month: KotlinMonth) : DateWithPrecision
    data class Date(val localDate: LocalDate) : DateWithPrecision
}

fun DateWithPrecision.toLocalDate() =
    when (this) {
        is DateWithPrecision.Year ->
            LocalDate(
                year = year,
                month = java.time.Month.JANUARY,
                dayOfMonth = 0
            )
        is DateWithPrecision.Month ->
            LocalDate(
                year = year,
                month = month,
                dayOfMonth = 0
            )
        is DateWithPrecision.Date ->
            this.localDate
    }