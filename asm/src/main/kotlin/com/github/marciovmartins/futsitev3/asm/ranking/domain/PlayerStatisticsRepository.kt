package com.github.marciovmartins.futsitev3.asm.ranking.domain

import java.util.UUID

interface PlayerStatisticsRepository {
    fun persist(processedGameDay: ProcessedGameDay)
    fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics
    fun delete(gameDayId: UUID)
}