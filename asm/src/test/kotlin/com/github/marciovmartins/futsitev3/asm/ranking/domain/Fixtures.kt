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

val player1: UUID = UUID.randomUUID()
val player2: UUID = UUID.randomUUID()
val player3: UUID = UUID.randomUUID()
val player4: UUID = UUID.randomUUID()
val player5: UUID = UUID.randomUUID()

val may1st2021: LocalDate = LocalDate.of(2021, 5, 1)
val may2nd2021: LocalDate = LocalDate.of(2021, 5, 2)
val may3rd2021: LocalDate = LocalDate.of(2021, 5, 3)
val may4th2021: LocalDate = LocalDate.of(2021, 5, 4)
val may5th2021: LocalDate = LocalDate.of(2021, 5, 5)

val may1st2021ProcessedGameDay = { amateurSoccerGroupId: UUID ->
    ProcessedGameDay(
        gameDayId = UUID.randomUUID(),
        amateurSoccerGroupId = amateurSoccerGroupId,
        date = may1st2021,
        playersStatistics = PlayersStatistics(
            matches = 2,
            items = setOf(
                PlayerStatistic(player1, 2, 1, 1, 0, 4, 1),
                PlayerStatistic(player2, 2, 1, 1, 0, 4, 1),
                PlayerStatistic(player3, 2, 0, 1, 1, 1, 4),
                PlayerStatistic(player4, 2, 0, 1, 1, 1, 4),
            ),
        ),
    )
}

val may2nd2021ProcessedGameDay = { amateurSoccerGroupId: UUID ->
    ProcessedGameDay(
        gameDayId = UUID.randomUUID(),
        amateurSoccerGroupId = amateurSoccerGroupId,
        date = may2nd2021,
        playersStatistics = PlayersStatistics(
            matches = 2,
            items = setOf(
                PlayerStatistic(player1, 2, 2, 0, 0, 3, 0),
                PlayerStatistic(player2, 2, 0, 0, 2, 0, 3),
                PlayerStatistic(player3, 2, 2, 0, 0, 3, 0),
                PlayerStatistic(player4, 2, 0, 2, 0, 2, 2),
            ),
        ),
    )
}

val may3rd2021ProcessedGameDay = { amateurSoccerGroupId: UUID ->
    ProcessedGameDay(
        gameDayId = UUID.randomUUID(),
        amateurSoccerGroupId = amateurSoccerGroupId,
        date = may3rd2021,
        playersStatistics = PlayersStatistics(
            matches = 2,
            items = setOf(
                PlayerStatistic(player1, 2, 0, 2, 0, 2, 2),
                PlayerStatistic(player2, 2, 0, 2, 0, 2, 2),
                PlayerStatistic(player3, 2, 0, 2, 0, 2, 2),
                PlayerStatistic(player5, 2, 0, 2, 0, 2, 2),
            ),
        ),
    )
}

val may4th2021ProcessedGameDay = { amateurSoccerGroupId: UUID ->
    ProcessedGameDay(
        gameDayId = UUID.randomUUID(),
        amateurSoccerGroupId = amateurSoccerGroupId,
        date = may4th2021,
        playersStatistics = PlayersStatistics(
            matches = 2,
            items = setOf(
                PlayerStatistic(player1, 1, 0, 1, 0, 1, 1),
                PlayerStatistic(player2, 1, 0, 1, 0, 1, 1),
                PlayerStatistic(player3, 1, 0, 1, 0, 1, 1),
                PlayerStatistic(player4, 1, 0, 1, 0, 1, 1),
            ),
        ),
    )
}

val may5th2021ProcessedGameDay = { amateurSoccerGroupId: UUID ->
    ProcessedGameDay(
        gameDayId = UUID.randomUUID(),
        amateurSoccerGroupId = amateurSoccerGroupId,
        date = may5th2021,
        playersStatistics = PlayersStatistics(
            matches = 2,
            items = setOf(
                PlayerStatistic(player1, 1, 1, 0, 0, 2, 1),
                PlayerStatistic(player2, 1, 0, 0, 0, 1, 2),
                PlayerStatistic(player3, 1, 0, 0, 0, 1, 2),
                PlayerStatistic(player5, 1, 1, 0, 0, 2, 1),
            ),
        ),
    )
}
