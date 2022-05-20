package com.github.marciovmartins.futsitev3.ranking.domain

import java.time.LocalDate
import java.util.UUID

interface PlayerStatisticsRepository {
    fun persist(amateurSoccerGroupId: UUID, gameDayDate: LocalDate, playersStatistics: PlayersStatistics)
    fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics
}