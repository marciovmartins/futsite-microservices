package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PointCriteriaDTO
import java.util.UUID

data class RankingCriteriaDTO(
    val amateurSoccerGroupId: UUID,
    val pointsCriterion: PointCriteriaDTO,
)
