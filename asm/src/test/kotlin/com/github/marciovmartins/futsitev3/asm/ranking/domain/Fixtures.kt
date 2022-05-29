package com.github.marciovmartins.futsitev3.asm.ranking.domain

import java.time.LocalDate
import java.util.UUID

fun defaultGameDay(
    player1: UUID = UUID.randomUUID(),
    player2: UUID = UUID.randomUUID(),
    player3: UUID = UUID.randomUUID(),
    player4: UUID = UUID.randomUUID(),
    gameDayId: UUID = UUID.randomUUID(),
    amateurSoccerGroupId: UUID = UUID.randomUUID(),
) = GameDay(
    gameDayId = gameDayId,
    amateurSoccerGroupId = amateurSoccerGroupId,
    date = LocalDate.now(),
    matches = setOf(
        Match(
            playerStatistics = setOf(
                PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
                PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
                PlayerStatistic(player4, 1, 0, 0, 1, 3, 8),
            )
        )
    )
)

val emptyPlayersStatistics = PlayersStatistics(matches = 0, items = emptySet())