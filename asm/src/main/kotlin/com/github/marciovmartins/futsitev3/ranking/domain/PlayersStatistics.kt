package com.github.marciovmartins.futsitev3.ranking.domain

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
                last = PlayerRanking(playerId = entry.playerStatistic.playerId, position = position, statistics = entry)
                last!!
            }.toSet()
            .let(::PlayersRanking)
    }

    private fun getPosition(lastPlayerRanking: PlayerRanking?, classification: String, index: Int): Int =
        if (lastPlayerRanking == null || lastPlayerRanking.statistics.classification != classification)
            index + 1
        else
            lastPlayerRanking.position
}
