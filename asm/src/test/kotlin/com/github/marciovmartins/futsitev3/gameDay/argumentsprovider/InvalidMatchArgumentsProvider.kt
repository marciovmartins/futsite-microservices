package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchPlayerDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object InvalidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // MATCH PLAYERS
        singleMatchArgument(
            testDescription = "invalid game day with exactly only one match player of team A",
            matchPlayers = setOf(
                matchPlayerDTO(team = A),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player for team A and one player for team B",
                field = "matches[].matchPlayers",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with exactly only one match player of team B",
            matchPlayers = setOf(
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player for team A and one player for team B",
                field = "matches[].matchPlayers",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with only match players from team A",
            matchPlayers = setOf(
                matchPlayerDTO(team = A),
                matchPlayerDTO(team = A),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player for team A and one player for team B",
                field = "matches[].matchPlayers",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with only match players from team B",
            matchPlayers = setOf(
                matchPlayerDTO(team = B),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must have at least one player for team A and one player for team B",
                field = "matches[].matchPlayers",
            ),
        ),
    )
}
