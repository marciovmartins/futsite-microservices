package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerRanking
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsByGameDay
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsByGameDay.Match.DRAW
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsByGameDay.Match.LOST
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsByGameDay.Match.WIN
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersRanking
import com.github.marciovmartins.futsitev3.ranking.domain.Ranking
import com.github.marciovmartins.futsitev3.ranking.infrastructure.TestDoubleGameDayRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class CalculateRankingTest {
    @Test
    fun `calculate ranking for zero game days`() {
        // setup
        val amateurSoccerGroupId = UUID.randomUUID()
        val gameDayRepository = TestDoubleGameDayRepository()
        val expectedRanking = Ranking(playersRanking = PlayersRanking(emptySet()))
        val options = CalculateRanking.Options(amateurSoccerGroupId = amateurSoccerGroupId)

        // execution
        val calculateRanking = CalculateRanking(gameDayRepository)
        val ranking = calculateRanking.with(options)

        // assertions
        assertThat(ranking)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRanking)
    }

    @Test
    fun `calculate ranking for one game day`() {
        // setup
        val amateurSoccerGroupId = UUID.randomUUID()
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val player4 = UUID.randomUUID()

        val gameDayRepository = TestDoubleGameDayRepository()
        val date1 = LocalDate.of(2021, 5, 1)
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player1, date1, WIN, 4, 0, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player2, date1, WIN, 3, 0, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player3, date1, LOST, 2, 1, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player4, date1, LOST, 1, 0, 0, 0, 0))

        val expectedRanking = Ranking(
            playersRanking = PlayersRanking(
                setOf(
                    PlayerRanking(
                        position = 1,
                        playerStatistic = PlayerStatistic(player1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5)
                    ),
                    PlayerRanking(
                        position = 1,
                        playerStatistic = PlayerStatistic(player2, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5)
                    ),
                    PlayerRanking(
                        position = 3,
                        playerStatistic = PlayerStatistic(player3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5)
                    ),
                    PlayerRanking(
                        position = 3,
                        playerStatistic = PlayerStatistic(player4, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5)
                    ),
                ),
            ),
        )

        val options = CalculateRanking.Options(amateurSoccerGroupId = amateurSoccerGroupId)

        // execution
        val calculateRanking = CalculateRanking(gameDayRepository)
        val ranking = calculateRanking.with(options)

        // assertions
        assertThat(ranking)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRanking)
    }

    @Test
    fun `calculate ranking for many`() {
        // setup
        val amateurSoccerGroupId = UUID.randomUUID()
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val player4 = UUID.randomUUID()
        val player5 = UUID.randomUUID()
        val marcioPreto = UUID.randomUUID()

        val gameDayRepository = TestDoubleGameDayRepository()
        val date1 = LocalDate.of(2021, 5, 1)
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player1, date1, WIN, 4, 0, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player2, date1, WIN, 3, 0, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player3, date1, LOST, 2, 1, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player4, date1, LOST, 1, 0, 0, 0, 0))
        val date2 = LocalDate.of(2021, 5, 2)
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player1, date2, WIN, 4, 0, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player3, date2, WIN, 3, 1, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player2, date2, LOST, 1, 0, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player4, date2, LOST, 2, 0, 0, 0, 0))
        val date3 = LocalDate.of(2021, 5, 3)
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player1, date3, DRAW, 2, 0, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player5, date3, DRAW, 3, 0, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player2, date3, DRAW, 1, 0, 0, 0, 0))
        gameDayRepository.persist(PlayerStatisticsByGameDay(amateurSoccerGroupId, player3, date3, DRAW, 4, 0, 0, 0, 0))

        val expectedRanking = Ranking(
            playersRanking = PlayersRanking(
                setOf(
                    PlayerRanking(
                        position = 1,
                        playerStatistic = PlayerStatistic(player1, "2,333 006 1008", 7, 3, 2, 1, 0, 20, 12, 8)
                    ),
                    PlayerRanking(
                        position = 2,
                        playerStatistic = PlayerStatistic(player2, "1,333 003 1002", 4, 3, 1, 1, 1, 17, 15, 2)
                    ),
                    PlayerRanking(
                        position = 3,
                        playerStatistic = PlayerStatistic(player3, "1,333 003 0998", 4, 3, 1, 1, 1, 15, 17, -2)
                    ),
                    PlayerRanking(
                        position = null,
                        playerStatistic = PlayerStatistic(marcioPreto, null, 0, 0, 0, 0, 0, 0, 0, 0)
                    ),
                    PlayerRanking(
                        position = null,
                        playerStatistic = PlayerStatistic(player5, null, 1, 1, 0, 1, 0, 5, 5, 0)
                    ),
                    PlayerRanking(
                        position = null,
                        playerStatistic = PlayerStatistic(player4, null, 0, 2, 0, 0, 2, 7, 15, -8)
                    ),
                ),
            ),
        )
        val options = CalculateRanking.Options(
            amateurSoccerGroupId = amateurSoccerGroupId
        )

        // execution
        val calculateRanking = CalculateRanking(gameDayRepository)
        val ranking = calculateRanking.with(options)

        // assertions
        assertThat(ranking)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedRanking)
    }
}