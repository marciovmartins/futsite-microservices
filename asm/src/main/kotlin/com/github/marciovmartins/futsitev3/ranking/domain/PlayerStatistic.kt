package com.github.marciovmartins.futsitev3.ranking.domain

import java.time.LocalDate
import java.util.UUID

data class PlayerStatistic(
    val playerId: UUID,
    val gameDayDate: LocalDate,
    val matches: Int,
    val victories: Int,
    val draws: Int,
    val defeats: Int,
    val goalsInFavor: Int,
    val goalsAgainst: Int,
)
