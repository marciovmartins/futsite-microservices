package com.github.marciovmartins.futsitev3.ranking.domain

import java.time.LocalDate
import java.util.UUID

data class PlayerStatistics(
    val player1: UUID,
    val date: LocalDate,
    val matches: Short,
    val victories: Short,
    val draws: Short,
    val defeats: Short,
    val goalsInFavor: Short,
    val goalsAgainst: Short
)