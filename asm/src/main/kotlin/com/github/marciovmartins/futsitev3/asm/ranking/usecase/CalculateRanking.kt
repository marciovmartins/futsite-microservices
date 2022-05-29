package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import com.github.marciovmartins.futsitev3.asm.ranking.domain.Percentage
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PointCriteria
import com.github.marciovmartins.futsitev3.asm.ranking.domain.Ranking
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CalculateRanking(private val playerStatisticsRepository: PlayerStatisticsRepository) {
    fun with(amateurSoccerGroupId: UUID, pointsCriterion: PointCriteriaDTO): RankingDTO {
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId)
        val ranking = playersStatistics.calculateRanking(pointsCriterion.toDomain())
        return ranking.toDTO()
    }
}

private fun PointCriteriaDTO.toDomain() = PointCriteria(
    victories = this.victories,
    draws = this.draws,
    defeats = this.defeats,
    percentage = this.percentage.let {
        Percentage(
            value = it.value,
            type = it.type.toDomain()
        )
    })

private fun Ranking.toDTO() = RankingDTO(
    playersRanking = this.playersRanking.items
        .map {
            PlayerRankingDTO(
                position = it.position,
                playerId = it.playerId,
                classification = it.classification,
                points = it.points,
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

private fun PercentageType.toDomain() = Percentage.Type.valueOf(this.name)