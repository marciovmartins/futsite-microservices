package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import java.util.UUID

class FakePlayerStatisticsRepository : PlayerStatisticsRepository {
    private val rows = mutableMapOf<UUID, PlayersStatistics>()

    override fun persist(amateurSoccerGroupId: UUID, playersStatistics: PlayersStatistics) {
        rows[amateurSoccerGroupId] = playersStatistics
    }

    override fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics {
        val playersStatistics = rows.getOrDefault(amateurSoccerGroupId, PlayersStatistics(emptySet(), 0))
        val matches = playersStatistics.matches
        return playersStatistics.items
            .groupBy { it.playerId }
            .mapValues { it.value.reduce(PlayerStatistic::add) }
            .values.toSet()
            .let { PlayersStatistics(it, matches) }
    }
}
