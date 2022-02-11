package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.playerDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object InvalidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
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
    )
}
