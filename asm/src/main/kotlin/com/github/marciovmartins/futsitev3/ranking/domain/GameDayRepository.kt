package com.github.marciovmartins.futsitev3.ranking.domain

interface GameDayRepository {
    fun persist(gameDayByPlayer: GameDayByPlayer)
}
