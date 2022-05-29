package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticsRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeletePlayerStatistic(
    private val playerStatisticsRepository: PlayerStatisticsRepository
) {
    fun with(gameDayId: UUID) {
        playerStatisticsRepository.delete(gameDayId)
    }
}
