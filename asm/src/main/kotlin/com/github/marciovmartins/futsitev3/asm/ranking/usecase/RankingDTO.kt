package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import java.util.UUID

data class RankingDTO(
    val playersRanking: Set<PlayerRankingDTO>,
)

data class PlayerRankingDTO(
    val playerId: UUID,
    val position: Long?,
    val classification: String?,
    val points: Long,
    val matches: Long,
    val victories: Long,
    val draws: Long,
    val defeats: Long,
    val goalsInFavor: Long,
    val goalsAgainst: Long,
    val goalsBalance: Long,
)
