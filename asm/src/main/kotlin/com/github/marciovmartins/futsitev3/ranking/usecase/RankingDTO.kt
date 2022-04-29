package com.github.marciovmartins.futsitev3.ranking.usecase

import java.util.UUID

data class RankingDTO(
    val playersRanking: Set<PlayerRankingDTO>,
)

data class PlayerRankingDTO(
    val playerId: UUID,
    val position: Int,
    val classification: String,
    val points: Int,
    val matches: Int,
    val victories: Int,
    val draws: Int,
    val defeats: Int,
    val goalsInFavor: Int,
    val goalsAgainst: Int,
    val goalsBalance: Int,
)
