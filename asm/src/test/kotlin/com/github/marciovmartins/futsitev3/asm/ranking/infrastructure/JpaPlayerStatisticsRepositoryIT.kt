package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.BaseIT
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.domain.ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.emptyPlayersStatistics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.UUID

class JpaPlayerStatisticsRepositoryIT : BaseIT() {

    @Autowired
    lateinit var playerStatisticsRepository: JpaPlayerStatisticsRepository

    @Test
    fun `retrieve empty player statistics`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val expectedPlayersStatistics = PlayersStatistics(matches = 0, items = emptySet())

        // when
        val playersStatistics = playerStatisticsRepository.findBy(amateurSoccerGroupId)

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .isEqualTo(expectedPlayersStatistics)
    }

    @Test
    fun `persist, retrieve, update and delete one game day date with many player statistics`() {
        // given
        val processedGameDay = ProcessedGameDay(
            gameDayId = UUID.randomUUID(),
            amateurSoccerGroupId = UUID.randomUUID(),
            date = LocalDate.now(),
            playersStatistics = PlayersStatistics(
                matches = 3,
                items = setOf(
                    PlayerStatistic(player1, 3, 2, 1, 0, 20, 12),
                    PlayerStatistic(player2, 3, 1, 1, 1, 17, 15),
                    PlayerStatistic(player3, 3, 1, 1, 1, 15, 17),
                    PlayerStatistic(player4, 2, 0, 0, 2, 7, 15),
                    PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
                ),
            ),
        )
        val expectedPlayersStatistics = PlayersStatistics(
            matches = 3,
            items = setOf(
                PlayerStatistic(player1, 3, 2, 1, 0, 20, 12),
                PlayerStatistic(player2, 3, 1, 1, 1, 17, 15),
                PlayerStatistic(player3, 3, 1, 1, 1, 15, 17),
                PlayerStatistic(player4, 2, 0, 0, 2, 7, 15),
                PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
            )
        )

        val expectedUpdatedPlayersStatistics = PlayersStatistics(
            matches = 3,
            items = setOf(
                PlayerStatistic(player1, 3, 2, 1, 0, 20, 12),
                PlayerStatistic(player2, 3, 1, 1, 1, 17, 15),
                PlayerStatistic(player3, 3, 1, 1, 1, 15, 17),
                PlayerStatistic(player4, 2, 0, 0, 2, 7, 15),
                PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
            )
        )
        val processedGameDayToUpdate = processedGameDay.copy(playersStatistics = expectedUpdatedPlayersStatistics)

        // when
        playerStatisticsRepository.persist(processedGameDay)
        val playersStatistics = playerStatisticsRepository.findBy(processedGameDay.amateurSoccerGroupId)
        playerStatisticsRepository.persist(processedGameDayToUpdate)
        val updatedPlayersStatistics = playerStatisticsRepository.findBy(processedGameDay.amateurSoccerGroupId)
        playerStatisticsRepository.delete(processedGameDay.gameDayId)
        val playersStatisticsAfterDeletion = playerStatisticsRepository.findBy(processedGameDay.amateurSoccerGroupId)

        // then
        assertThat(playersStatistics)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedPlayersStatistics)

        assertThat(updatedPlayersStatistics)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedUpdatedPlayersStatistics)

        assertThat(playersStatisticsAfterDeletion)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(emptyPlayersStatistics)
    }
}

private val player1 = UUID.randomUUID()
private val player2 = UUID.randomUUID()
private val player3 = UUID.randomUUID()
private val player4 = UUID.randomUUID()
private val player5 = UUID.randomUUID()
