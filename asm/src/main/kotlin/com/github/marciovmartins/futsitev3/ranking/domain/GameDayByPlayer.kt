package com.github.marciovmartins.futsitev3.ranking.domain

import java.time.LocalDate
import java.util.UUID

data class GameDayByPlayer(
    val amateurSoccerGroupId: UUID,
    val playerId: UUID,
    val date: LocalDate,
    val team: Team,
    val goalsInFavor: Short,
    val goalAgainst: Short,
    val yellowCards: Short,
    val blueCards: Short,
    val redCards: Short,
) {
    enum class Team {
        A, B
    }
}
