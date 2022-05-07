package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetPlayerStatistic(
    private val playerStatisticsRepository: PlayerStatisticsRepository,
    private val gameDayRepository: GameDayRepository
) {
    fun from(gameDayId: UUID) {
        val gameDay = gameDayRepository.findBy(gameDayId)
        val playersStatistics = gameDay.calculatePlayersStatistics()
        playerStatisticsRepository.persist(gameDay.amateurSoccerGroupId, playersStatistics)
    }
}
