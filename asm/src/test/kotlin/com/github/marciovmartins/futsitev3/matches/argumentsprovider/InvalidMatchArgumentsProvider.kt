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
        argument {
            val date = LocalDate.now().plusDays(1).toString()
            matchArgument(
                description = "invalid match with date in the future",
                matchDate = date,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "must be a date in the past or in the present",
                        field = "date",
                        invalidValue = date,
                    ),
                ),
            )
        },
        matchArgument(
            description = "invalid match with null date",
            matchDate = null,
            expectedException = arrayOf(
                ExpectedException(
                    message = "cannot be null",
                    field = "date",
                    invalidValue = null,
                )
            ),
        ),
        argument {
            val date = "invalid-format"
            matchArgument(
                description = "invalid match date with invalid date format",
                matchDate = date,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "Text 'invalid-format' could not be parsed at index 0",
                        field = "date",
                        invalidValue = date,
                    ),
                ),
            )
        },
        argument {
            val date = "03/12/2007"
            matchArgument(
                description = "invalid match date with wrong date in human readable format",
                matchDate = date,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "Text '03/12/2007' could not be parsed at index 0",
                        field = "date",
                        invalidValue = date
                    ),
                ),
            )
        },
        argument {
            val date = "1642941722026"
            matchArgument(
                description = "invalid match date with wrong date in millis",
                matchDate = date,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "Text '1642941722026' could not be parsed at index 0",
                        field = "date",
                        invalidValue = date
                    ),
                ),
            )
        },
        // QUOTE
        argument {
            val quote = faker.lorem().characters(256)
            matchArgument(
                description = "invalid match with quote exceeding 255 characters",
                matchQuote = quote,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "size must be between 0 and 255",
                        field = "quote",
                        invalidValue = quote,
                    ),
                ),
            )
        },
        // AUTHOR
        argument {
            val author = faker.lorem().characters(51)
            matchArgument(
                description = "invalid match with author exceeding 50 characters",
                matchAuthor = author,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "size must be between 0 and 50",
                        field = "author",
                        invalidValue = author,
                    ),
                ),
            )
        },
        // DESCRIPTION
        argument {
            val description = faker.lorem().characters(2049)
            matchArgument(
                description = "invalid match with description exceeding 2048 characters",
                matchDescription = description,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "size must be between 0 and 2048",
                        field = "description",
                        invalidValue = description,
                    ),
                ),
            )
        },
        // MATCH PLAYERS
        matchArgument(
            description = "invalid match with null match players",
            matchPlayers = null,
            expectedException = arrayOf(ExpectedException(message = "cannot be null", field = "matchPlayers")),
        ),
        matchArgument(
            description = "invalid match with empty match players",
            matchPlayers = emptySet(),
            expectedException = arrayOf(
                ExpectedException(
                    message = "must not be empty",
                    field = "matchPlayers",
                    invalidValue = emptyList<Any>(),
                )
            ),
        ),
        argument {
            val matchPlayers = setOf(
                matchPlayerDTO(team = A),
            )
            matchArgument(
                description = "invalid match with exactly only one match player of team A",
                matchPlayers = matchPlayers,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "must have at least one player for team A and one player for team B",
                        field = "matchPlayers",
                        invalidValue = matchPlayers.map { it.toMap() },
                    ),
                ),
            )
        },
        argument {
            val matchPlayers = setOf(
                matchPlayerDTO(team = B),
            )
            matchArgument(
                description = "invalid match with exactly only one match player of team B",
                matchPlayers = matchPlayers,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "must have at least one player for team A and one player for team B",
                        field = "matchPlayers",
                        invalidValue = matchPlayers.map { it.toMap() }
                    ),
                ),
            )
        },
        argument {
            val matchPlayers = setOf(
                matchPlayerDTO(team = A),
                matchPlayerDTO(team = A),
            )
            matchArgument(
                description = "invalid match with only match players from team A",
                matchPlayers = matchPlayers,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "must have at least one player for team A and one player for team B",
                        field = "matchPlayers",
                        invalidValue = matchPlayers.map { it.toMap() },
                    ),
                ),
            )
        },
        argument {
            val matchPlayers = setOf(
                matchPlayerDTO(team = B),
                matchPlayerDTO(team = B),
            )
            matchArgument(
                description = "invalid match with only match players from team B",
                matchPlayers = matchPlayers,
                expectedException = arrayOf(
                    ExpectedException(
                        message = "must have at least one player for team A and one player for team B",
                        field = "matchPlayers",
                        invalidValue = matchPlayers.map { it.toMap() },
                    ),
                ),
            )
        },
    )
}

private fun MatchPlayerDTO.toMap() = mapOf(
    "team" to team,
    "nickname" to nickname,
    "goalsInFavor" to goalsInFavor,
    "goalsAgainst" to goalsAgainst,
    "yellowCards" to yellowCards,
    "blueCards" to blueCards,
    "redCards" to redCards,
)
