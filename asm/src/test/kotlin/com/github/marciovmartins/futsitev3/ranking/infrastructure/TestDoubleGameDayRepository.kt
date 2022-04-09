package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.GameDayByPlayer
import com.github.marciovmartins.futsitev3.ranking.domain.GameDayRepository

class TestDoubleGameDayRepository : GameDayRepository {
    private val items = mutableSetOf<GameDayByPlayer>()

    override fun persist(gameDayByPlayer: GameDayByPlayer) {
        items.add(gameDayByPlayer)
    }
}
