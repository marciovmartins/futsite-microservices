package com.github.marciovmartins.futsitev3.matches.argumentsprovider

import com.github.marciovmartins.futsitev3.MyFaker.faker
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object InvalidMatchPlayerArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        matchArgument(
            description = "invalid match with null match players",
            matchPlayers = null,
            exceptionMessage = "cannot be null",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with empty match players",
            matchPlayers = emptySet(),
            exceptionMessage = "must not be empty",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with exactly only one match player of team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = A),
            ),
            exceptionMessage = "must have at least one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with exactly only one match player of team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "must have at least one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with only match players from team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = A),
                matchPlayerArgument(team = A),
            ),
            exceptionMessage = "must have at least one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with only match players from team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = B),
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "must have at least one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with invalid match player team value",
            matchPlayers = setOf(
                matchPlayerArgument(team = A),
                matchPlayerArgument(team = "C"),
            ),
            exceptionMessage = "must be one of the values accepted: [A, B]",
            exceptionField = "matchPlayers.1.team",
        ),
        matchArgument(
            description = "invalid match with null match player nickname",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, nickname = null),
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "cannot be null",
            exceptionField = "matchPlayers.0.nickname",
        ),
        matchArgument(
            description = "invalid match with blank match player nickname",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, nickname = "     "),
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "must not be blank",
            exceptionField = "nickname",
        ),
        matchArgument(
            description = "invalid match with match player nickname exceeding 50 characters",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, nickname = faker.lorem().characters(51)),
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "size must be between 1 and 50",
            exceptionField = "nickname",
        ),
    )
}