package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import java.time.LocalDate
import java.util.UUID

class DeletePlayerStatistic(
    private val playerStatisticsRepository: PlayerStatisticsRepository
) {
    fun with(amateurSoccerGroupId: UUID, gameDayDate: LocalDate) {
        playerStatisticsRepository.delete(amateurSoccerGroupId, gameDayDate)
    }
}
