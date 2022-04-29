package com.github.marciovmartins.futsitev3.ranking.domain

data class Ranking(
    val playersRanking: PlayersRanking
)

data class PlayersRanking(
    val items: Set<PlayerRanking>
)

data class PlayerRanking(
    val position: Int,
    val classification: String,
    val points: Int,
    val statistics: PlayerStatistic,
) {
    val playerId = statistics.playerId
}
