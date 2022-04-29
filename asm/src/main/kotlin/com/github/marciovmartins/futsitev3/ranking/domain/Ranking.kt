package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

data class Ranking(
    val playersRanking: PlayersRanking
)

data class PlayersRanking(
    val items: Set<PlayerRanking>
)

data class PlayerRanking(
    val playerId: UUID,
    val position: Int,
    val statistics: PlayerRankingStatistics,
)

data class PlayerRankingStatistics(
    val playerStatistic: PlayerStatistic,
    private val pointCriteria: PointCriteria,
) {
    val points =
        (playerStatistic.victories * pointCriteria.victories) + (playerStatistic.draws * pointCriteria.draws) + (playerStatistic.defeats * pointCriteria.defeats)
    val goalsBalance = playerStatistic.goalsInFavor - playerStatistic.goalsAgainst
    val classification = "%.3f %03d %04d".format(
        points.toFloat().div(playerStatistic.matches),
        playerStatistic.victories * pointCriteria.victories,
        1000 + goalsBalance
    )

}
