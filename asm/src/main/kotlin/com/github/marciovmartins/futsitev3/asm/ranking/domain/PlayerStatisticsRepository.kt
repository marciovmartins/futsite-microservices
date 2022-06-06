package com.github.marciovmartins.futsitev3.asm.ranking.domain

import com.github.marciovmartins.futsitev3.asm.shared.domain.LocalDateInterval
import java.util.UUID

interface PlayerStatisticsRepository {
    fun persist(processedGameDay: ProcessedGameDay)
    fun findBy(amateurSoccerGroupId: UUID, interval: LocalDateInterval): PlayersStatistics
    fun delete(gameDayId: UUID)
}