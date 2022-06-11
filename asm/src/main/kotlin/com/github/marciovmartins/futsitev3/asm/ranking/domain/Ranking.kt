package com.github.marciovmartins.futsitev3.asm.ranking.domain

data class Ranking(
    val playersRanking: PlayersRanking,
    val minimumMatches: Double,
)

data class PlayersRanking(
    val items: Set<PlayerRanking>,
)

data class PlayerRanking(
    val position: Long?,
    val classification: String?,
    val points: Long,
    val statistics: PlayerStatistic,
) {
    val playerId = statistics.playerId
}
