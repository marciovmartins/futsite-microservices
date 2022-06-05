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
        val playersRanking = getPlayersRanking(pointsCriteria)
        return Ranking(playersRanking)
    }

    private fun getPlayersRanking(pointsCriteria: PointCriteria): PlayersRanking {
        var last: PlayerRanking? = null
        return items.asSequence()
            .map { PlayerRankingStatistics(it, totalMatches, sumOfAllPlayerMatches, totalOfPlayers, pointsCriteria) }
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
        private val sumOfAllPlayerMatches: Double,
        private val totalOfPlayers: Double,
        private val pointCriteria: PointCriteria,
    ) {
        private val victoryPoints = playerStatistic.victories * pointCriteria.victories
        private val drawPoints = playerStatistic.draws * pointCriteria.draws
        private val defeatPoints = playerStatistic.defeats * pointCriteria.defeats
        private val sumOfAllPlayerMatchesByTotalOfPlayers = sumOfAllPlayerMatches / totalOfPlayers

        val points = victoryPoints + drawPoints + defeatPoints

        val classification: String? = if (isRanked()) {
            "%.3f %03d %04d".format(
                points.toFloat().div(playerStatistic.matches),
                playerStatistic.victories * pointCriteria.victories,
                1000 + (playerStatistic.goalsInFavor - playerStatistic.goalsAgainst)
            )
        } else null

        private fun isRanked(): Boolean {
            return when (pointCriteria.percentage.type) {
                Percentage.Type.BY_TOTAL ->
                    playerStatistic.matches >= matches * (pointCriteria.percentage.value * 0.01)
                Percentage.Type.BY_AVERAGE ->
                    playerStatistic.matches.toDouble() >= sumOfAllPlayerMatchesByTotalOfPlayers * (pointCriteria.percentage.value * 0.01)
            }
        }
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