package com.github.marciovmartins.futsitev3.matches.argumentsprovider

import com.github.marciovmartins.futsitev3.MyFaker.faker
import com.github.marciovmartins.futsitev3.matches.MatchPlayerFixture.matchPlayerDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.stream.Stream

object InvalidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // DATE
        matchArgument(
            description = "invalid match with date in the future",
            matchDate = LocalDate.now().plusDays(1).toString(),
            expectedException = setOf(
                ExpectedException(
                    message = "must be a date in the past or in the present",
                    field = "date",
                ),
            ),
        ),
        matchArgument(
            description = "invalid match with null date",
            matchDate = null,
            expectedException = setOf(
                ExpectedException(
                    message = "cannot be null",
                    field = "date",
                )
            ),
        ),
        matchArgument(
            description = "invalid match date with invalid date format",
            matchDate = "invalid-format",
            expectedException = setOf(
                ExpectedException(
                    message = "Text 'invalid-format' could not be parsed at index 0",
                    field = "date",
                ),
            ),
        ),
        matchArgument(
            description = "invalid match date with wrong date in human readable format",
            matchDate = "03/12/2007",
            expectedException = setOf(
                ExpectedException(
                    message = "Text '03/12/2007' could not be parsed at index 0",
                    field = "date"
                ),
            ),
        ),
        matchArgument(
            description = "invalid match date with wrong date in millis",
            matchDate = "1642941722026",
            expectedException = setOf(
                ExpectedException(
                    message = "Text '1642941722026' could not be parsed at index 0",
                    field = "date"
                ),
            ),
        ),
        // QUOTE
        matchArgument(
            description = "invalid match with quote exceeding 255 characters",
            matchQuote = faker.lorem().characters(256),
            expectedException = setOf(
                ExpectedException(
                    message = "size must be between 0 and 255",
                    field = "quote",
                ),
            ),
        ),
        // AUTHOR
        matchArgument(
            description = "invalid match with author exceeding 50 characters",
            matchAuthor = faker.lorem().characters(51),
            expectedException = setOf(
                ExpectedException(
                    message = "size must be between 0 and 50",
                    field = "author",
                ),
            ),
        ),
        // DESCRIPTION
        matchArgument(
            description = "invalid match with description exceeding 2048 characters",
            matchDescription = faker.lorem().characters(2049),
            expectedException = setOf(
                ExpectedException(
                    message = "size must be between 0 and 2048",
                    field = "description",
                ),
            ),
        ),
        // MATCH PLAYERS
        matchArgument(
            description = "invalid match with null match players",
            matchPlayers = null,
            expectedException = setOf(ExpectedException(message = "cannot be null", field = "matchPlayers")),
        ),
        matchArgument(
            description = "invalid match with empty match players",
            matchPlayers = emptySet(),
            expectedException = setOf(
                ExpectedException(
                    message = "must not be empty",
                    field = "matchPlayers",
                )
            ),
        ),
        matchArgument(
            description = "invalid match with exactly only one match player of team A",
            matchPlayers = setOf(
                matchPlayerDTO(team = A),
            ),
            expectedException = setOf(
                ExpectedException(
                    message = "must have at least one player for team A and one player for team B",
                    field = "matchPlayers",
                ),
            ),
        ),
        matchArgument(
            description = "invalid match with exactly only one match player of team B",
            matchPlayers = setOf(
                matchPlayerDTO(team = B),
            ),
            expectedException = setOf(
                ExpectedException(
                    message = "must have at least one player for team A and one player for team B",
                    field = "matchPlayers"
                ),
            ),
        ),
        matchArgument(
            description = "invalid match with only match players from team A",
            matchPlayers = setOf(
                matchPlayerDTO(team = A),
                matchPlayerDTO(team = A),
            ),
            expectedException = setOf(
                ExpectedException(
                    message = "must have at least one player for team A and one player for team B",
                    field = "matchPlayers",
                ),
            ),
        ),
        matchArgument(
            description = "invalid match with only match players from team B",
            matchPlayers = setOf(
                matchPlayerDTO(team = B),
                matchPlayerDTO(team = B),
            ),
            expectedException = setOf(
                ExpectedException(
                    message = "must have at least one player for team A and one player for team B",
                    field = "matchPlayers",
                ),
            ),
        ),
        matchArgument(
            description = "invalid match with with multiple errors",
            matchDate = LocalDate.now().plusDays(1).toString(),
            matchQuote = faker.lorem().characters(256),
            expectedException = setOf(
                ExpectedException(
                    message = "must be a date in the past or in the present",
                    field = "date",
                ),
                ExpectedException(
                    message = "size must be between 0 and 255",
                    field = "quote",
                ),
            ),
        ),
    )
}
