package com.github.marciovmartins.futsitev3.asm.ranking.usecase.argumentsprovider

import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.domain.ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may1st2021
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may1st2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may2nd2021
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may2nd2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may3rd2021
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may3rd2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may4th2021
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may4th2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.may5th2021ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player1
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player2
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player3
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player4
import com.github.marciovmartins.futsitev3.asm.ranking.domain.player5
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.DateIntervalDTO
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PercentageDTO
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PercentageType
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PlayerRankingDTO
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.PointCriteriaDTO
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.RankingDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.UUID
import java.util.stream.Stream

object ValidCalculateRanking : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            testDescription = "with empty processed game days",
            processedGameDays = emptySet(),
            expectedPlayersRanking = emptySet(),
            minimumMatches = 0.0,
        ),
        argument(
            testDescription = "with one game day with one match",
            processedGameDays = setOf(
                processedGameDayArgument(
                    playersStatistics = PlayersStatistics(
                        matches = 1,
                        items = setOf(
                            PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
                            PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
                            PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
                            PlayerStatistic(player4, 1, 0, 0, 1, 3, 8),
                        )
                    ),
                ),
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                PlayerRankingDTO(player2, 1, "3,000 003 1005", 3, 1, 1, 0, 0, 8, 3, 5),
                PlayerRankingDTO(player3, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
                PlayerRankingDTO(player4, 3, "0,000 000 0995", 0, 1, 0, 0, 1, 3, 8, -5),
            ),
            minimumMatches = 0.001,
        ),
        argument(
            testDescription = "with many game days with one match each",
            processedGameDays = processedGameDaysArgument(
                playersStatisticsByDate = setOf(
                    Pair(
                        may1st2021, PlayersStatistics(
                            matches = 1,
                            items = setOf(
                                PlayerStatistic(player1, 1, 1, 0, 0, 8, 3),
                                PlayerStatistic(player2, 1, 1, 0, 0, 8, 3),
                                PlayerStatistic(player3, 1, 0, 0, 1, 3, 8),
                                PlayerStatistic(player4, 1, 0, 0, 1, 3, 8),
                            ),
                        )
                    ),
                    Pair(
                        may2nd2021, PlayersStatistics(
                            matches = 1,
                            items = setOf(
                                PlayerStatistic(player1, 1, 1, 0, 0, 7, 4),
                                PlayerStatistic(player2, 1, 0, 0, 1, 4, 7),
                                PlayerStatistic(player3, 1, 1, 0, 0, 7, 4),
                                PlayerStatistic(player4, 1, 0, 0, 1, 4, 7),
                            ),
                        )
                    ),
                    Pair(
                        may3rd2021, PlayersStatistics(
                            matches = 1,
                            items = setOf(
                                PlayerStatistic(player1, 1, 0, 1, 0, 5, 5),
                                PlayerStatistic(player2, 1, 0, 1, 0, 5, 5),
                                PlayerStatistic(player3, 1, 0, 1, 0, 5, 5),
                                PlayerStatistic(player5, 1, 0, 1, 0, 5, 5),
                            ),
                        )
                    ),
                )
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "2,333 006 1008", 7, 3, 2, 1, 0, 20, 12, 8),
                PlayerRankingDTO(player2, 2, "1,333 003 1002", 4, 3, 1, 1, 1, 17, 15, 2),
                PlayerRankingDTO(player3, 3, "1,333 003 0998", 4, 3, 1, 1, 1, 15, 17, -2),
                PlayerRankingDTO(player5, 4, "1,000 000 1000", 1, 1, 0, 1, 0, 5, 5, 0),
                PlayerRankingDTO(player4, 5, "0,000 000 0992", 0, 2, 0, 0, 2, 7, 15, -8),
            ),
            minimumMatches = 0.003,
        ),
        argument(
            testDescription = "with many default game days with many default match each",
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
            pointsCriteria = PointCriteriaDTO(
                victories = 4,
                draws = 2,
                defeats = 1,
                percentage = PercentageDTO(value = 0.1, type = PercentageType.BY_TOTAL),
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "3,000 012 1006", 18, 6, 3, 3, 0, 9, 3, 6),
                PlayerRankingDTO(player3, 2, "2,500 008 1000", 15, 6, 2, 3, 1, 6, 6, 0),
                PlayerRankingDTO(player2, 3, "2,000 004 1000", 12, 6, 1, 3, 2, 6, 6, 0),
                PlayerRankingDTO(player5, 4, "2,000 000 1000", 4, 2, 0, 2, 0, 2, 2, 0),
                PlayerRankingDTO(player4, 5, "1,750 000 0997", 7, 4, 0, 3, 1, 3, 6, -3),
            ),
        ),
        argument(
            testDescription = "with 100% of the total matches",
            pointsCriteria = PointCriteriaDTO(
                victories = 3,
                draws = 1,
                defeats = 0,
                percentage = PercentageDTO(value = 100.0, type = PercentageType.BY_TOTAL),
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "2,000 009 1006", 12, 6, 3, 3, 0, 9, 3, 6),
                PlayerRankingDTO(player3, 2, "1,500 006 1000", 9, 6, 2, 3, 1, 6, 6, 0),
                PlayerRankingDTO(player2, 3, "1,000 003 1000", 6, 6, 1, 3, 2, 6, 6, 0),
                PlayerRankingDTO(player5, null, null, 2, 2, 0, 2, 0, 2, 2, 0),
                PlayerRankingDTO(player4, null, null, 3, 4, 0, 3, 1, 3, 6, -3),
            ),
            minimumMatches = 6.0,
        ),
        argument(
            testDescription = "with 80% of the total matches",
            pointsCriteria = PointCriteriaDTO(
                victories = 3,
                draws = 1,
                defeats = 0,
                percentage = PercentageDTO(value = 80.0, type = PercentageType.BY_TOTAL)
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "2,000 009 1006", 12, 6, 3, 3, 0, 9, 3, 6),
                PlayerRankingDTO(player3, 2, "1,500 006 1000", 9, 6, 2, 3, 1, 6, 6, 0),
                PlayerRankingDTO(player2, 3, "1,000 003 1000", 6, 6, 1, 3, 2, 6, 6, 0),
                PlayerRankingDTO(player5, null, null, 2, 2, 0, 2, 0, 2, 2, 0),
                PlayerRankingDTO(player4, null, null, 3, 4, 0, 3, 1, 3, 6, -3),
            ),
            minimumMatches = 4.8,
        ),
        argument(
            testDescription = "with 100% of the average of all matches by total of players",
            pointsCriteria = PointCriteriaDTO(
                victories = 3,
                draws = 1,
                defeats = 0,
                percentage = PercentageDTO(value = 100.0, type = PercentageType.BY_AVERAGE)
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "2,000 009 1006", 12, 6, 3, 3, 0, 9, 3, 6),
                PlayerRankingDTO(player3, 2, "1,500 006 1000", 9, 6, 2, 3, 1, 6, 6, 0),
                PlayerRankingDTO(player2, 3, "1,000 003 1000", 6, 6, 1, 3, 2, 6, 6, 0),
                PlayerRankingDTO(player5, null, null, 2, 2, 0, 2, 0, 2, 2, 0),
                PlayerRankingDTO(player4, null, null, 3, 4, 0, 3, 1, 3, 6, -3),
            ),
            minimumMatches = 4.8,
        ),
        argument(
            testDescription = "with 80% of the average of all matches by total of players",
            pointsCriteria = PointCriteriaDTO(
                victories = 3,
                draws = 1,
                defeats = 0,
                percentage = PercentageDTO(value = 80.0, type = PercentageType.BY_AVERAGE)
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "2,000 009 1006", 12, 6, 3, 3, 0, 9, 3, 6),
                PlayerRankingDTO(player3, 2, "1,500 006 1000", 9, 6, 2, 3, 1, 6, 6, 0),
                PlayerRankingDTO(player2, 3, "1,000 003 1000", 6, 6, 1, 3, 2, 6, 6, 0),
                PlayerRankingDTO(player4, 4, "0,750 000 0997", 3, 4, 0, 3, 1, 3, 6, -3),
                PlayerRankingDTO(player5, null, null, 2, 2, 0, 2, 0, 2, 2, 0),
            ),
            minimumMatches = 3.84,
        ),
        argument(
            testDescription = "with interval",
            interval = DateIntervalDTO(may2nd2021, may4th2021),
            processedGameDays = setOf(
                may1st2021ProcessedGameDay,
                may2nd2021ProcessedGameDay,
                may3rd2021ProcessedGameDay,
                may4th2021ProcessedGameDay,
                may5th2021ProcessedGameDay,
            ),
            expectedPlayersRanking = setOf(
                PlayerRankingDTO(player1, 1, "1,800 006 1003", 9, 5, 2, 3, 0, 6, 3, 3),
                PlayerRankingDTO(player3, 1, "1,800 006 1003", 9, 5, 2, 3, 0, 6, 3, 3),
                PlayerRankingDTO(player4, 3, "1,000 000 1000", 3, 3, 0, 3, 0, 3, 3, 0),
                PlayerRankingDTO(player5, 3, "1,000 000 1000", 2, 2, 0, 2, 0, 2, 2, 0),
                PlayerRankingDTO(player2, 5, "0,600 000 0997", 3, 5, 0, 3, 2, 3, 6, -3),
            ),
        )
    )
}

private fun argument(
    testDescription: String,
    amateurSoccerGroupId: UUID = UUID.randomUUID(),
    interval: DateIntervalDTO = DateIntervalDTO(LocalDate.MIN, LocalDate.MAX),
    processedGameDays: Set<(UUID) -> ProcessedGameDay> = setOf(
        may1st2021ProcessedGameDay,
        may2nd2021ProcessedGameDay,
        may3rd2021ProcessedGameDay,
    ),
    pointsCriteria: PointCriteriaDTO = PointCriteriaDTO(
        victories = 3,
        draws = 1,
        defeats = 0,
        percentage = PercentageDTO(
            value = 0.1,
            type = PercentageType.BY_TOTAL
        )
    ),
    expectedPlayersRanking: Set<PlayerRankingDTO>,
    minimumMatches: Double = 0.006
) = Arguments.of(
    testDescription,
    amateurSoccerGroupId,
    interval,
    processedGameDays.map { it(amateurSoccerGroupId) }.toSet(),
    pointsCriteria,
    RankingDTO(expectedPlayersRanking, minimumMatches)
)

private fun processedGameDayArgument(
    date: LocalDate = LocalDate.now(),
    playersStatistics: PlayersStatistics,
) = { amateurSoccerGroupId: UUID ->
    ProcessedGameDay(
        gameDayId = UUID.randomUUID(),
        amateurSoccerGroupId = amateurSoccerGroupId,
        date = date,
        playersStatistics = playersStatistics,
    )
}

private fun processedGameDaysArgument(
    playersStatisticsByDate: Set<Pair<LocalDate, PlayersStatistics>>
): Set<(UUID) -> ProcessedGameDay> = playersStatisticsByDate.map {
    processedGameDayArgument(date = it.first, playersStatistics = it.second)
}.toSet()
