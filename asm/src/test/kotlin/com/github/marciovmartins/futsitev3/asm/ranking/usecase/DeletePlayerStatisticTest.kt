package com.github.marciovmartins.futsitev3.asm.ranking.usecase

import com.github.marciovmartins.futsitev3.asm.ranking.domain.defaultProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.infrastructure.FakePlayerStatisticsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DeletePlayerStatisticTest {
    @Test
    fun `with game day with one match`() {
        // given
        val playerStatisticsRepository = FakePlayerStatisticsRepository()
        val processedGameDay = defaultProcessedGameDay()
        playerStatisticsRepository.persist(processedGameDay)

        val deletePlayerStatistic = DeletePlayerStatistic(playerStatisticsRepository)

        // when
        deletePlayerStatistic.with(processedGameDay.gameDayId)

        // then
        assertThat(playerStatisticsRepository.exists(processedGameDay.gameDayId)).isFalse
    }
}