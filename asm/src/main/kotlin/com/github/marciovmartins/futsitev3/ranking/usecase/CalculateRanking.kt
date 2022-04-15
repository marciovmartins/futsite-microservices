package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.ranking.domain.Ranking
import java.util.UUID

class CalculateRanking(private val playerStatisticsRepository: PlayerStatisticsRepository) {
    fun with(amateurSoccerGroupId: UUID, pointsCriteria: PointCriteriaDTO): RankingDTO {
        val ranking = playerStatisticsRepository.findBy(amateurSoccerGroupId)
        return ranking.toDTO()
    }
}

private fun Ranking.toDTO() = RankingDTO(
    playersRanking = this.playersRanking.items
        .map {
            PlayerRankingDTO(
                position = it.position,
                playerId = it.playerId,
                classification = it.statistics.classification,
                points = it.statistics.points,
                matches = it.statistics.matches,
                victories = it.statistics.victories,
                draws = it.statistics.draws,
                defeats = it.statistics.defeats,
                goalsInFavor = it.statistics.goalsInFavor,
                goalsAgainst = it.statistics.goalsAgainst,
                goalsBalance = it.statistics.goalsBalance,
            )
        }
        .toSet()
)
