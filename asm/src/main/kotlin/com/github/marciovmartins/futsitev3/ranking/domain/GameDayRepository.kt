package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

interface GameDayRepository {
    fun findBy(gameDayId: UUID): GameDay
}
