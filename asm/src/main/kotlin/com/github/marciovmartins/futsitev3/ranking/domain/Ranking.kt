package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

data class Ranking(
    val playersRanking: PlayersRanking
)

data class PlayersRanking(
    val items: Set<PlayerRanking>
) {
    companion object {
        fun from(items: Set<PlayerStatistic>): PlayersRanking {
            val playersRanking = mutableSetOf<PlayerRanking>()

            val playerId2playerRankingStatistics =
                items.associateBy(PlayerStatistic::playerId) { PlayerRankingStatistics(it) }
                    .toList().sortedBy { (_, v) -> v.classification }.reversed().toMap()

            for ((index, entry) in playerId2playerRankingStatistics.entries.withIndex()) {
                val position =
                    if (playersRanking.isEmpty() || playersRanking.last().statistics.classification != entry.value.classification)
                        index + 1
                    else
                        playersRanking.last().position
                playersRanking.add(PlayerRanking(playerId = entry.key, position = position, statistics = entry.value))
            }
            return PlayersRanking(playersRanking)
        }
    }
}

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
    val victoryPoints = victories * 3
    val goalsBalance = goalsInFavor - goalsAgainst
    val classification =
        "%.3f %03d %04d".format(victoryPoints.toFloat().div(matches), victoryPoints, 1000 + goalsBalance)

    constructor(playerStatistic: PlayerStatistic) : this(
        matches = playerStatistic.matches,
        victories = playerStatistic.victories,
        draws = playerStatistic.draws,
        defeats = playerStatistic.defeats,
        goalsInFavor = playerStatistic.goalsInFavor,
        goalsAgainst = playerStatistic.goalsAgainst,
    )
}
