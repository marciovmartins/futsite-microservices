package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class PlayerStatisticsJpaRepositoryIT {

    @Autowired
    lateinit var playerStatisticsRepository: PlayerStatisticsRepository

    @Test
    fun `retrieve empty player statistics`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val expectedPlayersStatistics = PlayersStatistics(items = emptySet())

        // when
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId)

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .isEqualTo(expectedPlayersStatistics)
    }
}