package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.GameDayByPlayer
import com.github.marciovmartins.futsitev3.ranking.domain.GameDayByPlayer.Team
import com.github.marciovmartins.futsitev3.ranking.domain.Player
import com.github.marciovmartins.futsitev3.ranking.domain.Players
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

        val expectedRanking = Ranking(players = Players(emptySet()))
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
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player1, date1, Team.A, 4, 0, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player2, date1, Team.A, 3, 0, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player3, date1, Team.B, 2, 1, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player4, date1, Team.B, 1, 0, 0, 0, 0))

        val expectedRanking = Ranking(
            players = Players(
                setOf(
                    Player(
                        position = 1,
                        playerId = player1,
                        classification = "1,000 003 1008",
                        points = 3,
                        matches = 1,
                        victories = 1,
                        draws = 0,
                        defeats = 0,
                        goalsInFavor = 8,
                        goalsAgainst = 3,
                        goalsBalance = 5,
                    ),
                    Player(
                        position = 1,
                        playerId = player2,
                        classification = "1,000 003 1008",
                        points = 3,
                        matches = 1,
                        victories = 1,
                        draws = 0,
                        defeats = 0,
                        goalsInFavor = 8,
                        goalsAgainst = 3,
                        goalsBalance = 5,
                    ),
                    Player(
                        position = 3,
                        playerId = player3,
                        classification = "0,000 000 0995",
                        points = 0,
                        matches = 1,
                        victories = 0,
                        draws = 0,
                        defeats = 1,
                        goalsInFavor = 3,
                        goalsAgainst = 8,
                        goalsBalance = -5,
                    ),
                    Player(
                        position = 3,
                        playerId = player4,
                        classification = null,
                        points = 0,
                        matches = 2,
                        victories = 0,
                        draws = 0,
                        defeats = 2,
                        goalsInFavor = 7,
                        goalsAgainst = 15,
                        goalsBalance = -8,
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
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player1, date1, Team.A, 4, 0, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player2, date1, Team.A, 3, 0, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player3, date1, Team.B, 2, 1, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player4, date1, Team.B, 1, 0, 0, 0, 0))
        val date2 = LocalDate.of(2021, 5, 2)
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player1, date2, Team.A, 4, 0, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player3, date2, Team.A, 3, 1, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player2, date2, Team.B, 1, 0, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player4, date2, Team.B, 2, 0, 0, 0, 0))
        val date3 = LocalDate.of(2021, 5, 3)
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player1, date3, Team.A, 2, 0, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player5, date3, Team.A, 3, 0, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player2, date3, Team.B, 1, 0, 0, 0, 0))
        gameDayRepository.persist(GameDayByPlayer(amateurSoccerGroupId, player3, date3, Team.B, 4, 0, 0, 0, 0))

        val expectedRanking = Ranking(
            players = Players(
                setOf(
                    Player(
                        position = 1,
                        playerId = player1,
                        classification = "2,333 006 1008",
                        points = 7,
                        matches = 3,
                        victories = 2,
                        draws = 1,
                        defeats = 0,
                        goalsInFavor = 20,
                        goalsAgainst = 12,
                        goalsBalance = 8,
                    ),
                    Player(
                        position = 2,
                        playerId = player2,
                        classification = "1,333 003 1002",
                        points = 4,
                        matches = 3,
                        victories = 1,
                        draws = 1,
                        defeats = 1,
                        goalsInFavor = 17,
                        goalsAgainst = 15,
                        goalsBalance = 2,
                    ),
                    Player(
                        position = 3,
                        playerId = player3,
                        classification = "1,333 003 0998",
                        points = 4,
                        matches = 3,
                        victories = 1,
                        draws = 1,
                        defeats = 1,
                        goalsInFavor = 15,
                        goalsAgainst = 17,
                        goalsBalance = -2,
                    ),
                    Player(
                        position = null,
                        playerId = marcioPreto,
                        classification = null,
                        points = 0,
                        matches = 0,
                        victories = 0,
                        draws = 0,
                        defeats = 0,
                        goalsInFavor = 0,
                        goalsAgainst = 0,
                        goalsBalance = 0,
                    ),
                    Player(
                        position = null,
                        playerId = player5,
                        classification = null,
                        points = 1,
                        matches = 1,
                        victories = 0,
                        draws = 1,
                        defeats = 0,
                        goalsInFavor = 5,
                        goalsAgainst = 5,
                        goalsBalance = 0,
                    ),
                    Player(
                        position = null,
                        playerId = player4,
                        classification = null,
                        points = 0,
                        matches = 2,
                        victories = 0,
                        draws = 0,
                        defeats = 2,
                        goalsInFavor = 7,
                        goalsAgainst = 15,
                        goalsBalance = -8,
                    ),
                )
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