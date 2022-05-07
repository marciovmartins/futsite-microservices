package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

interface PlayerStatisticsRepository {
    fun persist(amateurSoccerGroupId: UUID, playersStatistics: PlayersStatistics)
    fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics
}