package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticsRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdatePlayerStatic(
    private val playerStatisticRepository: PlayerStatisticsRepository,
    private val gameDayRepository: GameDayRepository
) {
    fun with(gameDayId: UUID) {
        val processedGameDayToUpdate = gameDayRepository.findBy(gameDayId)
            ?: TODO("Not implemented yet")
        playerStatisticRepository.persist(processedGameDayToUpdate)
    }
}
