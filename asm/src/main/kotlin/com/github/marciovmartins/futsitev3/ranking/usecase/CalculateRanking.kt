package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import java.util.UUID

class CalculateRanking(playerStatisticsRepository: PlayerStatisticsRepository) {
    fun with(amateurSoccerGroupId: UUID): RankingDTO {
        return RankingDTO(playersRanking = emptySet())
    }
}
