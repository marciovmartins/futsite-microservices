package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.defaultGameDay
import com.github.marciovmartins.futsitev3.ranking.infrastructure.FakePlayerStatisticsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DeletePlayerStatisticTest {
    @Test
    fun `with game day with one match`() {
        // given
        val playerStatisticsRepository = FakePlayerStatisticsRepository()
        val gameDay = defaultGameDay()
        playerStatisticsRepository.persist(gameDay)

        val deletePlayerStatistic = DeletePlayerStatistic(playerStatisticsRepository)

        // when
        deletePlayerStatistic.with(gameDay.gameDayId)

        // then
        assertThat(playerStatisticsRepository.exists(gameDay.gameDayId)).isFalse
    }
}