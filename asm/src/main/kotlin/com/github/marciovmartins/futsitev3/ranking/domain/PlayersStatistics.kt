package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

data class PlayersStatistics(
    val items: Set<PlayerStatistic>
) {
    fun calculateRanking(pointsCriteria: PointCriteria): Ranking {
        val playersRanking = getPlayersRanking(pointsCriteria)
        return Ranking(playersRanking)
    }

    private fun getPlayersRanking(pointsCriteria: PointCriteria): PlayersRanking {
        var last: PlayerRanking? = null
        return items.asSequence()
            .map { PlayerRankingStatistics(it, pointsCriteria) }.toList()
            .sortedByDescending { playerStatistic -> playerStatistic.classification }
            .mapIndexed { index, entry ->
                val position = getPosition(last, entry.classification, index)
                last = PlayerRanking(
                    position = position,
                    classification = entry.classification,
                    points = entry.points,
                    statistics = entry.playerStatistic
                )
                last!!
            }.toSet()
            .let(::PlayersRanking)
    }

    private fun getPosition(lastPlayerRanking: PlayerRanking?, classification: String, index: Int): Int =
        if (lastPlayerRanking == null || lastPlayerRanking.classification != classification)
            index + 1
        else
            lastPlayerRanking.position

    private data class PlayerRankingStatistics(
        val playerStatistic: PlayerStatistic,
        private val pointCriteria: PointCriteria,
    ) {
        val points =
            (playerStatistic.victories * pointCriteria.victories) + (playerStatistic.draws * pointCriteria.draws) + (playerStatistic.defeats * pointCriteria.defeats)
        val classification = "%.3f %03d %04d".format(
            points.toFloat().div(playerStatistic.matches),
            playerStatistic.victories * pointCriteria.victories,
            1000 + (playerStatistic.goalsInFavor - playerStatistic.goalsAgainst)
        )
    }
}

data class PlayerStatistic(
    val playerId: UUID,
    val matches: Int,
    val victories: Int,
    val draws: Int,
    val defeats: Int,
    val goalsInFavor: Int,
    val goalsAgainst: Int,
) {
    val goalsBalance = goalsInFavor - goalsAgainst

    companion object {
        fun add(a: PlayerStatistic, b: PlayerStatistic) = PlayerStatistic(
            playerId = a.playerId,
            matches = a.matches + b.matches,
            victories = a.victories + b.victories,
            draws = a.draws + b.draws,
            defeats = a.defeats + b.defeats,
            goalsInFavor = a.goalsInFavor + b.goalsInFavor,
            goalsAgainst = a.goalsAgainst + b.goalsAgainst,
        )
    }
}