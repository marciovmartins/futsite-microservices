package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.usecase.CalculateRanking
import com.github.marciovmartins.futsitev3.ranking.usecase.RankingDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CalculateRankingController(
    private val calculateRanking: CalculateRanking,
) {
    @PostMapping(path = ["/statistics/players"])
    fun calculatePlayerStatistics(@RequestBody rankingCriteriaDTO: RankingCriteriaDTO): RankingDTO {
        return calculateRanking.with(
            amateurSoccerGroupId = rankingCriteriaDTO.amateurSoccerGroupId,
            pointsCriterion = rankingCriteriaDTO.pointsCriterion,
        )
    }
}