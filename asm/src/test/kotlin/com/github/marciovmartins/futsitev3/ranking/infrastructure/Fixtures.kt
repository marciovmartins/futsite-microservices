package com.github.marciovmartins.futsitev3.ranking.infrastructure

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