package bilal.altify.data.spotify.model.util

import bilal.altify.domain.model.DateWithPrecision
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

fun getDateWithPrecision(date: String, precision: String) =
    when (precision) {
        "year" -> DateWithPrecision.Year(date.toInt())
        "month" ->
            DateWithPrecision.Month(
                year = date.substringBefore('-').toInt(),
                month = Month.of(date.substringAfter('-').toInt())
            )
        "day" ->
            DateWithPrecision.Date(localDate = LocalDate.parse(date))
        else -> { throw Exception() }
    }