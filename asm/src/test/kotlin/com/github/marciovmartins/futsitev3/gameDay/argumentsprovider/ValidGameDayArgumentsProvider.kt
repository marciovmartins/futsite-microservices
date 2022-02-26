package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.stream.Stream

object ValidGameDayArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // GENERAL
        gameDayArgument(
            testDescription = "valid game day with minimum data",
        ),

        // DATE
        gameDayArgument(
            testDescription = "valid game day with date in the present",
            date = LocalDate.now().toString(),
        ),
        gameDayArgument(
            testDescription = "valid game day with date in the past",
            date = LocalDate.now().minusDays(1).toString(),
        ),
        // MATCHES
        gameDayArgument(
            testDescription = "valid game day with one valid match",
            matches = setOf(matchDTO()),
        ),
        gameDayArgument(
            testDescription = "valid game day with several valid matches",
            matches = setOf(
                matchDTO(order = 1),
                matchDTO(order = 2)
            ),
        ),
    )
}