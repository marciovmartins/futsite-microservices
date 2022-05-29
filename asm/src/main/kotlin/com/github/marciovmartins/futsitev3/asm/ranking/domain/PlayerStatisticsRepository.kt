package com.github.marciovmartins.futsitev3.asm.ranking.domain

import java.time.LocalDate
import java.util.UUID

interface PlayerStatisticsRepository {
    fun persist(
        amateurSoccerGroupId: UUID,
        gameDayId: UUID,
        gameDayDate: LocalDate,
        playersStatistics: PlayersStatistics
    )
    fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics
    fun delete(gameDayId: UUID)
}