package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

interface GameDayRepository {
    fun persist(playerStatisticsByGameDay: PlayerStatisticsByGameDay)
    fun findBy(amateurSoccerGroupId: UUID): PlayersRanking
}
