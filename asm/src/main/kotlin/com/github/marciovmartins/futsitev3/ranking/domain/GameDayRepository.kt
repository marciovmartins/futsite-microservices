package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

interface GameDayRepository {
    fun persist(gameDayByPlayer: GameDayByPlayer)
    fun findBy(amateurSoccerGroupId: UUID): Players
}
