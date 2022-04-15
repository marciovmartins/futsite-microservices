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
    val matches: Int,
    val victories: Int,
    val draws: Int,
    val defeats: Int,
    val goalsInFavor: Int,
    val goalsAgainst: Int,
) {
    val points = (victories * 3) + (draws * 1)
    val goalsBalance = goalsInFavor - goalsAgainst
    val classification = "%.3f %03d %04d".format(points.toFloat().div(matches), victories * 3, 1000 + goalsBalance)

    constructor(playerStatistic: PlayerStatistic) : this(
        matches = playerStatistic.matches,
        victories = playerStatistic.victories,
        draws = playerStatistic.draws,
        defeats = playerStatistic.defeats,
        goalsInFavor = playerStatistic.goalsInFavor,
        goalsAgainst = playerStatistic.goalsAgainst,
    )

    companion object {
        fun add(a: PlayerRankingStatistics, b: PlayerRankingStatistics) = PlayerRankingStatistics(
            matches = a.matches + b.matches,
            victories = a.victories + b.victories,
            draws = a.draws + b.draws,
            defeats = a.defeats + b.defeats,
            goalsInFavor = a.goalsInFavor + b.goalsInFavor,
            goalsAgainst = a.goalsAgainst + b.goalsAgainst,
        )
    }
}
