package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

interface PlayerStatisticsRepository {
    fun persist(amateurSoccerGroupId: UUID, playerStatistic: PlayerStatistic)
    fun findBy(amateurSoccerGroupId: UUID): Ranking
}