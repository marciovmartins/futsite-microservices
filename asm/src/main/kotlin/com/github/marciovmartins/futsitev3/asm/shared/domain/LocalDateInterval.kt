package com.github.marciovmartins.futsitev3.asm.shared.domain

import java.time.LocalDate

class LocalDateInterval(
    val beginInclusive: LocalDate,
    val endInclusive: LocalDate
) {
    fun contains(date: LocalDate): Boolean = (date.isEqual(beginInclusive) || date.isEqual(endInclusive))
            || (date.isAfter(beginInclusive) && date.isBefore(endInclusive))

}
