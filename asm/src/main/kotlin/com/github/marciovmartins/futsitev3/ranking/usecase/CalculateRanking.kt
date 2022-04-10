package com.github.marciovmartins.futsitev3.ranking.usecase

import java.util.UUID

class CalculateRanking {
    fun with(amateurSoccerGroupId: UUID): RankingDTO {
        return RankingDTO(playersRanking = emptySet())
    }
}
