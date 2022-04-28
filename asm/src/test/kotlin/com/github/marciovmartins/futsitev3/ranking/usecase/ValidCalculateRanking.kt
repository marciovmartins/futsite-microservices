package com.github.marciovmartins.futsitev3.ranking.usecase

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.UUID
import java.util.stream.Stream

object ValidCalculateRanking : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            testDescription = "with different parameters",
            playersStatistics = emptySet(),
            expectedPlayersRanking = emptySet(),
        ),
        argument(
            testDescription = "with one game day with one match",
            playersStatistics = setOf(
                Pair(may1st2021, PlayerStatistic(player1, 1, 1, 0, 0, 8, 3)),
                Pair(may1st2021, PlayerStatistic(player2, 1, 1, 0, 0, 8, 3)),
                Pair(may1st2021, PlayerStatistic(player3, 1, 0, 0, 1, 3, 8)),
                Pair(may1st2021, PlayerStatistic(player4, 1, 0, 0, 1, 3, 8)),
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                PlayerRankingDTO(player2, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                PlayerRankingDTO(player3, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
                PlayerRankingDTO(player4, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
            ),
        ),
        argument(
            testDescription = "with many game days with one match each",
            playersStatistics = setOf(
                // player 1
                Pair(may1st2021, PlayerStatistic(player1, 1, 1, 0, 0, 8, 3)),
                Pair(may2nd2021, PlayerStatistic(player1, 1, 1, 0, 0, 7, 4)),
                Pair(may3rd2021, PlayerStatistic(player1, 1, 0, 1, 0, 5, 5)),
                // player 2
                Pair(may1st2021, PlayerStatistic(player2, 1, 1, 0, 0, 8, 3)),
                Pair(may2nd2021, PlayerStatistic(player2, 1, 0, 0, 1, 4, 7)),
                Pair(may3rd2021, PlayerStatistic(player2, 1, 0, 1, 0, 5, 5)),
                // player 3
                Pair(may1st2021, PlayerStatistic(player3, 1, 0, 0, 1, 3, 8)),
                Pair(may2nd2021, PlayerStatistic(player3, 1, 1, 0, 0, 7, 4)),
                Pair(may3rd2021, PlayerStatistic(player3, 1, 0, 1, 0, 5, 5)),
                // player 4
                Pair(may1st2021, PlayerStatistic(player4, 1, 0, 0, 1, 3, 8)),
                Pair(may2nd2021, PlayerStatistic(player4, 1, 0, 0, 1, 4, 7)),
                // player 5
                Pair(may3rd2021, PlayerStatistic(player5, 1, 0, 1, 0, 5, 5)),
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "2,333 006 1008", 7, 3, 2, 1, 0, 20, 12, 8),
                PlayerRankingDTO(player2, 2, "1,333 003 1002", 4, 3, 1, 1, 1, 17, 15, 2),
                PlayerRankingDTO(player3, 3, "1,333 003 0998", 4, 3, 1, 1, 1, 15, 17, -2),
                PlayerRankingDTO(player5, 4, "1,000 000 1000", 1, 1, 0, 1, 0, 5, 5, 0),
                PlayerRankingDTO(player4, 5, "0,000 000 0992", 0, 2, 0, 0, 2, 7, 15, -8),
            ),
        ),
        argument(
            testDescription = "with many game days with many match each",
            playersStatistics = defaultPlayersStatistics,
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "2,000 009 1006", 12, 6, 3, 3, 0, 9, 3, 6),
                PlayerRankingDTO(player3, 2, "1,500 006 1000", 9, 6, 2, 3, 1, 6, 6, 0),
                PlayerRankingDTO(player2, 3, "1,000 003 1000", 6, 6, 1, 3, 2, 6, 6, 0),
                PlayerRankingDTO(player5, 4, "1,000 000 1000", 2, 2, 0, 2, 0, 2, 2, 0),
                PlayerRankingDTO(player4, 5, "0,750 000 0997", 3, 4, 0, 3, 1, 3, 6, -3),
            ),
        ),
        argument(
            testDescription = "with different point criteria",
            playersStatistics = defaultPlayersStatistics,
            pointsCriteria = PointCriteriaDTO(victories = 4, draws = 2, defeats = 1),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "3,000 012 1006", 18, 6, 3, 3, 0, 9, 3, 6),
                PlayerRankingDTO(player3, 2, "2,500 008 1000", 15, 6, 2, 3, 1, 6, 6, 0),
                PlayerRankingDTO(player2, 3, "2,000 004 1000", 12, 6, 1, 3, 2, 6, 6, 0),
                PlayerRankingDTO(player5, 4, "2,000 000 1000", 4, 2, 0, 2, 0, 2, 2, 0),
                PlayerRankingDTO(player4, 5, "1,750 000 0997", 7, 4, 0, 3, 1, 3, 6, -3),
            ),
        ),
    )
}

private fun argument(
    testDescription: String,
    playersStatistics: Set<Pair<LocalDate, PlayerStatistic>>,
    pointsCriteria: PointCriteriaDTO = PointCriteriaDTO(victories = 3, draws = 1, defeats = 0),
    expectedPlayersRanking: Set<PlayerRankingDTO>,
) = Arguments.of(testDescription, playersStatistics, pointsCriteria, RankingDTO(expectedPlayersRanking))

private val player1 = UUID.randomUUID()
private val player2 = UUID.randomUUID()
private val player3 = UUID.randomUUID()
private val player4 = UUID.randomUUID()
private val player5 = UUID.randomUUID()

val may1st2021: LocalDate = LocalDate.of(2021, 5, 1)
val may2nd2021: LocalDate = LocalDate.of(2021, 5, 2)
val may3rd2021: LocalDate = LocalDate.of(2021, 5, 3)

private val defaultPlayersStatistics = setOf(
    // player 1
    Pair(may1st2021, PlayerStatistic(player1, 2, 1, 1, 0, 4, 1)),
    Pair(may2nd2021, PlayerStatistic(player1, 2, 2, 0, 0, 3, 0)),
    Pair(may3rd2021, PlayerStatistic(player1, 2, 0, 2, 0, 2, 2)),
    // player 2
    Pair(may1st2021, PlayerStatistic(player2, 2, 1, 1, 0, 4, 1)),
    Pair(may2nd2021, PlayerStatistic(player2, 2, 0, 0, 2, 0, 3)),
    Pair(may3rd2021, PlayerStatistic(player2, 2, 0, 2, 0, 2, 2)),
    // player 3
    Pair(may1st2021, PlayerStatistic(player3, 2, 0, 1, 1, 1, 4)),
    Pair(may2nd2021, PlayerStatistic(player3, 2, 2, 0, 0, 3, 0)),
    Pair(may3rd2021, PlayerStatistic(player3, 2, 0, 2, 0, 2, 2)),
    // player 4
    Pair(may1st2021, PlayerStatistic(player4, 2, 0, 1, 1, 1, 4)),
    Pair(may2nd2021, PlayerStatistic(player4, 2, 0, 2, 0, 2, 2)),
    // player 5
    Pair(may3rd2021, PlayerStatistic(player5, 2, 0, 2, 0, 2, 2)),
)
