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

data class RankingDTO(
    val playersRanking: Set<PlayerRankingDTO>,
)

data class PlayerRankingDTO(
    val playerId: UUID,
    val position: Long?,
    val classification: String?,
    val points: Long,
    val matches: Long,
    val victories: Long,
    val draws: Long,
    val defeats: Long,
    val goalsInFavor: Long,
    val goalsAgainst: Long,
    val goalsBalance: Long,
)

data class PointCriteriaDTO(
    val victories: Long,
    val draws: Long,
    val defeats: Long,
    val percentage: PercentageDTO,
)

data class PercentageDTO(
    val value: Double,
    val type: PercentageType,
)

enum class PercentageType {
    /**
     * For an athlete to have his classification calculated and to be ranked, he needs to have the number of matches
     * greater than a percentage of the sum of the matches played by the participants in the period divided by the
     * number of participants in the period.
     */
    BY_AVERAGE,

    /**
     * For an athlete to be ranked, the number of matches must be greater than a percentage of the total matches played
     * in the period.
     */
    BY_TOTAL
}

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

private fun PercentageType.toDomain() = Percentage.Type.valueOf(this.name)
