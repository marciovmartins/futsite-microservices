package com.github.marciovmartins.futsitev3.asm.ranking.domain

import java.time.LocalDate
import java.util.UUID

fun defaultProcessedGameDay(
    player1: UUID = UUID.randomUUID(),
    player2: UUID = UUID.randomUUID(),
    player3: UUID = UUID.randomUUID(),
    player4: UUID = UUID.randomUUID(),
    gameDayId: UUID = UUID.randomUUID(),
    amateurSoccerGroupId: UUID = UUID.randomUUID(),
) = ProcessedGameDay(
    gameDayId = gameDayId,
    amateurSoccerGroupId = amateurSoccerGroupId,
    date = LocalDate.now(),
    playersStatistics = PlayersStatistics(
        matches = 1,
        items = setOf(
            PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
            PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
            PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
            PlayerStatistic(player4, 1, 0, 0, 1, 3, 8),
        ),
    ),
)

fun defaultGameDay(
    player1: UUID,
    player2: UUID,
    player3: UUID,
    player4: UUID,
    gameDayId: UUID = UUID.randomUUID(),
    amateurSoccerGroupId: UUID = UUID.randomUUID(),
) = com.github.marciovmartins.futsitev3.asm.gameDay.GameDay(
    id = gameDayId,
    amateurSoccerGroupId = amateurSoccerGroupId,
    date = LocalDate.now(),
    matches = setOf(
        com.github.marciovmartins.futsitev3.asm.gameDay.Match(
            order = 1,
            playerStatistics = setOf(
                com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic(
                    null,
                    com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic.Team.A,
                    player1,
                    4,
                    0,
                    0,
                    0,
                    0
                ),
                com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic(
                    null,
                    com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic.Team.A,
                    player2,
                    3,
                    0,
                    0,
                    0,
                    0
                ),
                com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic(
                    null,
                    com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic.Team.B,
                    player3,
                    2,
                    1,
                    0,
                    0,
                    0
                ),
                com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic(
                    null,
                    com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic.Team.B,
                    player4,
                    1,
                    0,
                    0,
                    0,
                    0
                ),
            ),
        ),
    ),
)

val emptyPlayersStatistics = PlayersStatistics(matches = 0, items = emptySet())