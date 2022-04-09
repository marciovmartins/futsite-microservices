package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

data class Player(
    val position: Short?,
    val playerId: UUID,
    val classification: String?,
    val points: Short,
    val matches: Short,
    val victories: Short,
    val draws: Short,
    val defeats: Short,
    val goalsInFavor: Short,
    val goalsAgainst: Short,
    val goalsBalance: Short
)

