package com.github.marciovmartins.futsitev3.ranking.infrastructure

import java.time.LocalDate
import java.util.UUID

data class TestRankingCriteriaDTO(val amateurSoccerGroupId: UUID, val pointsCriterion: PointsCriterionDTO) {
    data class PointsCriterionDTO(val victories: Long, val draws: Long, val defeats: Long)
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

data class TestGameDayDTO(
    val gameDayId: Any?,
    val amateurSoccerGroupId: Any?,
    val date: Any?,
    val quote: Any?,
    val author: Any?,
    val description: Any?,
    val matches: Set<TestMatchDTO>?
)

data class TestMatchDTO(
    val order: Any?,
    val playerStatistics: Set<TestPlayerStatisticDTO>?,
)

data class TestPlayerStatisticDTO(
    val team: Any?,
    val playerId: Any?,
    val goalsInFavor: Any?,
    val goalsAgainst: Any?,
    val yellowCards: Any?,
    val blueCards: Any?,
    val redCards: Any?,
)

fun defaultGameDayResponse(
    player1: UUID,
    player2: UUID,
    player3: UUID,
    player4: UUID,
    gameDayId: UUID = UUID.randomUUID(),
    amateurSoccerGroupId: UUID = UUID.randomUUID(),
) = TestGameDayDTO(
    gameDayId = gameDayId,
    amateurSoccerGroupId = amateurSoccerGroupId,
    date = LocalDate.now(),
    quote = null,
    author = null,
    description = null,
    matches = setOf(
        TestMatchDTO(
            order = 1,
            playerStatistics = setOf(
                TestPlayerStatisticDTO("A", player1, 4, 0, 0, 0, 0),
                TestPlayerStatisticDTO("A", player2, 3, 0, 0, 0, 0),
                TestPlayerStatisticDTO("B", player3, 2, 1, 0, 0, 0),
                TestPlayerStatisticDTO("B", player4, 1, 0, 0, 0, 0),
            ),
        )
    ),
)