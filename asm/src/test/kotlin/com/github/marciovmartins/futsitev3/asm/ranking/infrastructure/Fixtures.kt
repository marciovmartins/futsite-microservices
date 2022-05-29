package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.gameDay.GameDay
import com.github.marciovmartins.futsitev3.asm.gameDay.Match
import com.github.marciovmartins.futsitev3.asm.gameDay.PlayerStatistic
import java.time.LocalDate
import java.util.UUID

data class TestRankingCriteriaDTO(
    val amateurSoccerGroupId: UUID,
    val pointsCriterion: PointsCriterionDTO = PointsCriterionDTO()
) {
    data class PointsCriterionDTO(
        val victories: Long = 3,
        val draws: Long = 1,
        val defeats: Long = 0,
        val percentage: PercentageDTO = PercentageDTO(value = 0.1, type = "BY_TOTAL"),
    )

    data class PercentageDTO(val value: Double, val type: String)
}

data class ExpectedResponseBody(
    val title: String,
    val status: Int,
    val violations: Set<ExpectedException>? = null,
    val detail: String? = null,
)

data class ExpectedException(
    val message: String,
    val field: String? = null,
)

fun defaultAsmGameDay(
    player1: UUID,
    player2: UUID,
    player3: UUID,
    player4: UUID,
    gameDayId: UUID = UUID.randomUUID(),
    amateurSoccerGroupId: UUID = UUID.randomUUID(),
) = GameDay(
    id = gameDayId,
    amateurSoccerGroupId = amateurSoccerGroupId,
    date = LocalDate.now(),
    matches = setOf(
        Match(
            order = 1,
            playerStatistics = setOf(
                PlayerStatistic(null, PlayerStatistic.Team.A, player1, 4, 0, 0, 0, 0),
                PlayerStatistic(null, PlayerStatistic.Team.A, player2, 3, 0, 0, 0, 0),
                PlayerStatistic(null, PlayerStatistic.Team.B, player3, 2, 1, 0, 0, 0),
                PlayerStatistic(null, PlayerStatistic.Team.B, player4, 1, 0, 0, 0, 0),
            ),
        ),
    ),
)