package com.github.marciovmartins.futsitev3.asm.ranking.domain

import java.util.UUID

data class PlayersStatistics(
    val matches: Int,
    val items: Set<PlayerStatistic>
) {
    private val totalMatches = matches.toDouble()
    private val sumOfAllPlayerMatches = items.sumOf { it.matches }.toDouble()
    private val totalOfPlayers = items.count().toDouble()

    fun calculateRanking(pointsCriteria: PointCriteria): Ranking {
        val minimumMatches = when (pointsCriteria.percentage.type) {
            Percentage.Type.BY_TOTAL -> matches * (pointsCriteria.percentage.value * 0.01)
            Percentage.Type.BY_AVERAGE -> (sumOfAllPlayerMatches / totalOfPlayers) * (pointsCriteria.percentage.value * 0.01)
        }

        var last: PlayerRanking? = null
        val playersRanking = items.asSequence()
            .map { PlayerRankingStatistics(it, totalMatches, minimumMatches, pointsCriteria) }
            .toList()
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
        return Ranking(playersRanking, minimumMatches)
    }

    private fun getPosition(lastPlayerRanking: PlayerRanking?, classification: String?, index: Int): Long? =
        if (lastPlayerRanking == null || (classification != null && lastPlayerRanking.classification != classification))
            (index + 1).toLong()
        else if (classification != null)
            lastPlayerRanking.position
        else null

    private data class PlayerRankingStatistics(
        val playerStatistic: PlayerStatistic,
        private val matches: Double,
        private val minimumMatches: Double,
        private val pointCriteria: PointCriteria,
    ) {
        private val victoryPoints = playerStatistic.victories * pointCriteria.victories
        private val drawPoints = playerStatistic.draws * pointCriteria.draws
        private val defeatPoints = playerStatistic.defeats * pointCriteria.defeats

        val points = victoryPoints + drawPoints + defeatPoints

        val classification: String? = if (playerStatistic.matches >= minimumMatches) {
            "%.3f %03d %04d".format(
                points.toFloat().div(playerStatistic.matches),
                playerStatistic.victories * pointCriteria.victories,
                1000 + (playerStatistic.goalsInFavor - playerStatistic.goalsAgainst)
            )
        } else null
    }
}

data class PlayerStatistic(
    val playerId: UUID,
    val matches: Long,
    val victories: Long,
    val draws: Long,
    val defeats: Long,
    val goalsInFavor: Long,
    val goalsAgainst: Long,
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