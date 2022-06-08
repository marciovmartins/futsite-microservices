package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import java.time.LocalDate
import java.util.UUID

data class TestRankingCriteriaDTO(
    val amateurSoccerGroupId: UUID,
    val interval: TestLocalDateInterval,
    val pointsCriterion: PointsCriterionDTO = PointsCriterionDTO()
) {
    data class TestLocalDateInterval(
        val from: LocalDate,
        val to: LocalDate,
    )

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
