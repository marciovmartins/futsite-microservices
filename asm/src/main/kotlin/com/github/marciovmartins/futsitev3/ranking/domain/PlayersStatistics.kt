package com.github.marciovmartins.futsitev3.ranking.domain

data class PlayersStatistics(
    val items: Set<PlayerStatistic>
) {
    fun calculateRanking(pointsCriteria: PointCriteria): Ranking {
        val playersRanking = getPlayersRanking()
        return Ranking(playersRanking)
    }

    private fun getPlayersRanking(): PlayersRanking {
        var last: PlayerRanking? = null
        return items.groupBy(PlayerStatistic::playerId) { PlayerRankingStatistics(it) }
            .mapValues { it.value.reduce(PlayerRankingStatistics::add) }
            .toList().sortedByDescending { (_, playerRankingStatistics) -> playerRankingStatistics.classification }
            .toMap().entries.mapIndexed { index, entry ->
                val position = getPosition(last, entry.value.classification, index)
                last = PlayerRanking(playerId = entry.key, position = position, statistics = entry.value)
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
