package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.A
import com.github.marciovmartins.futsitev3.gameDay.B
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.testPlayerStatisticDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object InvalidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // ORDER
        singleMatchArgument(
            testDescription = "invalid game day with match order equal to zero",
            order = 0,
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 1",
                field = "matches[].order",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with negative match order",
            order = -1,
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 1",
                field = "matches[].order",
            ),
        ),
        // MATCH PLAYERS
        singleMatchArgument(
            testDescription = "invalid game day with exactly only one match player statistics of team A",
            playerStatistics = setOf(
                testPlayerStatisticDTO(team = A),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player statistic for team A and one player statistic for team B",
                field = "matches[].playerStatistics",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with exactly only one match player statistics of team B",
            playerStatistics = setOf(
                testPlayerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player statistic for team A and one player statistic for team B",
                field = "matches[].playerStatistics",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with only match player statistics from team A",
            playerStatistics = setOf(
                testPlayerStatisticDTO(team = A),
                testPlayerStatisticDTO(team = A),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player statistic for team A and one player statistic for team B",
                field = "matches[].playerStatistics",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with only match player statistics from team B",
            playerStatistics = setOf(
                testPlayerStatisticDTO(team = B),
                testPlayerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player statistic for team A and one player statistic for team B",
                field = "matches[].playerStatistics",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with duplicated player user id in different team",
            playerStatistics = setOf(
                testPlayerStatisticDTO(team = A, playerId = "d636819a-7df6-4c07-90ff-e4a781530500"),
                testPlayerStatisticDTO(team = B, playerId = "d636819a-7df6-4c07-90ff-e4a781530500"),
            ),
            expectedExceptions = singleExpectedException(
                message = "cannot have duplicated player user id",
                field = "matches[].playerStatistics"
            )
        ),
        singleMatchArgument(
            testDescription = "invalid game day with duplicated player user id on same team",
            playerStatistics = setOf(
                testPlayerStatisticDTO(team = A, playerId = "f70897f4-497b-48f2-8094-18464886195b"),
                testPlayerStatisticDTO(team = A, playerId = "f70897f4-497b-48f2-8094-18464886195b"),
                testPlayerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "cannot have duplicated player user id",
                field = "matches[].playerStatistics"
            )
        ),
    )
}
