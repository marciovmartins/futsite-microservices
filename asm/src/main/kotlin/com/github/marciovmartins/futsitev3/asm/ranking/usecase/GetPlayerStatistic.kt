package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticsRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetPlayerStatistic(
    private val playerStatisticsRepository: PlayerStatisticsRepository,
    private val gameDayRepository: GameDayRepository
) {
    fun from(gameDayId: UUID) {
        val processedGameDay = gameDayRepository.findBy(gameDayId)
            ?: throw Exception("not implemented yet")
        playerStatisticsRepository.persist(processedGameDay)
    }
}
