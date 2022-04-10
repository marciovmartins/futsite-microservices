package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.GameDayByPlayer
import com.github.marciovmartins.futsitev3.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.ranking.domain.Players
import java.util.UUID

class TestDoubleGameDayRepository : GameDayRepository {
    private val items = mutableSetOf<GameDayByPlayer>()

    override fun persist(gameDayByPlayer: GameDayByPlayer) {
        items.add(gameDayByPlayer)
    }

    override fun findBy(amateurSoccerGroupId: UUID): Players {
        TODO("Not yet implemented")
    }
}
