package com.github.marciovmartins.futsitev3.asm.shared.domain

import java.time.LocalDate

class LocalDateInterval(
    private val beginInclusive: LocalDate,
    private val endInclusive: LocalDate
) {
    fun contains(date: LocalDate): Boolean = (date.isEqual(beginInclusive) || date.isEqual(endInclusive))
            || (date.isAfter(beginInclusive) && date.isBefore(endInclusive))

}
