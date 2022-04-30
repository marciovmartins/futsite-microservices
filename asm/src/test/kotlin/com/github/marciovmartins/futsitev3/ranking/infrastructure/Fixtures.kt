package com.github.marciovmartins.futsitev3.ranking.infrastructure

import java.util.UUID

data class TestRankingCriteriaDTO(val amateurSoccerGroupId: UUID, val pointsCriterion: PointsCriterionDTO) {
    data class PointsCriterionDTO(val victories: Long, val draws: Long, val defeats: Long)
}