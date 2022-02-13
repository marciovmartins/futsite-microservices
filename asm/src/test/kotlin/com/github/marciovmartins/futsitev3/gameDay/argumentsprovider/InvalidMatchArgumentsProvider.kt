package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.playerDTO
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
            testDescription = "invalid game day with exactly only one match player of team A",
            players = setOf(
                playerDTO(team = A),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player for team A and one player for team B",
                field = "matches[].players",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with exactly only one match player of team B",
            players = setOf(
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player for team A and one player for team B",
                field = "matches[].players",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with only match players from team A",
            players = setOf(
                playerDTO(team = A),
                playerDTO(team = A),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player for team A and one player for team B",
                field = "matches[].players",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with only match players from team B",
            players = setOf(
                playerDTO(team = B),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player for team A and one player for team B",
                field = "matches[].players",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with duplicated player user id in different team",
            players = setOf(
                playerDTO(team = A, userId = "d636819a-7df6-4c07-90ff-e4a781530500"),
                playerDTO(team = B, userId = "d636819a-7df6-4c07-90ff-e4a781530500"),
            ),
            expectedExceptions = singleExpectedException(
                message = "cannot have duplicated player user id",
                field = "matches[].players"
            )
        ),
        singleMatchArgument(
            testDescription = "invalid game day with duplicated player user id on same team",
            players = setOf(
                playerDTO(team = A, userId = "f70897f4-497b-48f2-8094-18464886195b"),
                playerDTO(team = A, userId = "f70897f4-497b-48f2-8094-18464886195b"),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "cannot have duplicated player user id",
                field = "matches[].players"
            )
        ),
    )
}
