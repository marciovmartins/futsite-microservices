package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.ExpectedException
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.stream.Stream

object InvalidGameDayArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // DATE
        gameDayArgument(
            testDescription = "invalid game day with date in the future",
            date = LocalDate.now().plusDays(1).toString(),
            expectedException = setOf(
                ExpectedException(
                    message = "must be a date in the past or in the present",
                    field = "date",
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "invalid game day with null date",
            date = null,
            expectedException = setOf(
                ExpectedException(
                    message = "cannot be null",
                    field = "date",
                )
            ),
        ),
        gameDayArgument(
            testDescription = "invalid game day date with invalid date format",
            date = "invalid-format",
            expectedException = setOf(
                ExpectedException(
                    message = "Text 'invalid-format' could not be parsed at index 0",
                    field = "date",
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "invalid game day date with wrong date in human readable format",
            date = "03/12/2007",
            expectedException = setOf(
                ExpectedException(
                    message = "Text '03/12/2007' could not be parsed at index 0",
                    field = "date"
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "invalid game day date with wrong date in millis",
            date = "1642941722026",
            expectedException = setOf(
                ExpectedException(
                    message = "Text '1642941722026' could not be parsed at index 0",
                    field = "date"
                ),
            ),
        ),
        // MATCHES
        gameDayArgument(
            testDescription = "invalid game day with null matches",
            matches = null,
            expectedException = setOf(ExpectedException(message = "cannot be null", field = "matches")),
        ),
        gameDayArgument(
            testDescription = "invalid game day with empty matches",
            matches = emptySet(),
            expectedException = setOf(
                ExpectedException(
                    message = "must not be empty",
                    field = "matches",
                )
            ),
        ),
        gameDayArgument(
            testDescription = "invalid game day with duplicated match order",
            matches = setOf(
                matchDTO(order = 1),
                matchDTO(order = 1),
            ),
            expectedException = setOf(
                ExpectedException(
                    message = "must have valid match with sequential order",
                    field = "matches",
                )
            ),
        ),
        gameDayArgument(
            testDescription = "invalid game day with skip match orders",
            matches = setOf(
                matchDTO(order = 1),
                matchDTO(order = 3),
            ),
            expectedException = setOf(
                ExpectedException(
                    message = "must have match order sequentially",
                    field = "matches",
                )
            ),
        ),
    )
}
