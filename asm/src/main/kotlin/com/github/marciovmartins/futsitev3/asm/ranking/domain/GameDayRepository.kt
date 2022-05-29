package com.github.marciovmartins.futsitev3.asm.ranking.domain

import java.util.UUID

interface GameDayRepository {
    fun findBy(gameDayId: UUID): ProcessedGameDay?
}
