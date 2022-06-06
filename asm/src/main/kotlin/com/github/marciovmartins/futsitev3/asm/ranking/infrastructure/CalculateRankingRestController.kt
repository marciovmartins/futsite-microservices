package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.ranking.usecase.CalculateRanking
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PointCriteriaDTO
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.RankingDTO
import com.github.marciovmartins.futsitev3.asm.shared.domain.LocalDateInterval
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
class CalculateRankingRestController(
    private val calculateRanking: CalculateRanking,
) {
    @PostMapping(path = ["/statistics/players"])
    fun calculatePlayerStatistics(@RequestBody rankingCriteriaDTO: RankingCriteriaDTO): RankingDTO {
        return calculateRanking.with(
            amateurSoccerGroupId = rankingCriteriaDTO.amateurSoccerGroupId,
            interval = LocalDateInterval(LocalDate.MIN, LocalDate.MAX),
            pointsCriterion = rankingCriteriaDTO.pointsCriterion,
        )
    }
}

data class RankingCriteriaDTO(
    val amateurSoccerGroupId: UUID,
    val pointsCriterion: PointCriteriaDTO,
)