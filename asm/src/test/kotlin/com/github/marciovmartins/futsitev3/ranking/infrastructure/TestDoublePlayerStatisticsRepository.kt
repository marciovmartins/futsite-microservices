package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import java.util.UUID

class TestDoublePlayerStatisticsRepository : PlayerStatisticsRepository {
    private val rows = mutableMapOf<UUID, MutableSet<PlayerStatistic>>()

    override fun persist(amateurSoccerGroupId: UUID, playerStatistic: PlayerStatistic) {
        val playersStatistics = rows.getOrPut(amateurSoccerGroupId) { mutableSetOf() }
        playersStatistics += playerStatistic
    }

    override fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics {
        return rows.getOrDefault(amateurSoccerGroupId, emptySet())
            .let(::PlayersStatistics)
    }
}
