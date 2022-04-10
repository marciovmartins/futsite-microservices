package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistics
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository

class TestDoublePlayerStatisticsRepository : PlayerStatisticsRepository {
    private val items = mutableSetOf<PlayerStatistics>()

    override fun persist(playerStatistics: PlayerStatistics) {
        items += playerStatistics
    }
}
