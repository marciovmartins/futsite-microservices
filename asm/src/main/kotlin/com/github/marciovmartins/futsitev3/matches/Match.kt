package com.github.marciovmartins.futsitev3.matches

import java.time.LocalDate

class Match(
    val date: LocalDate,
    val quote: String?,
    val author: String?,
    val description: String?
) {
    init {
        require(date.isBefore(LocalDate.now().plusDays(1))) { "Date must be today or in the past" }
    }
}
