package com.github.marciovmartins.futsitev3.matches.argumentsprovider

import com.github.marciovmartins.futsitev3.MyFaker.faker
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object ValidMatchPlayerArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        matchArgument(
            description = "valid match with match player nickname with minimum of 1 character",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, nickname = faker.lorem().characters(1)),
                matchPlayerArgument(team = B)
            ),
        ),
        matchArgument(
            description = "valid match with match player nickname with maximum of 50 character",
            matchDescription = faker.lorem().characters(50),
        ),
        matchArgument(
            description = "valid match with match player goals in favor with minimum amount of 1",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, goalsInFavor = 1),
                matchPlayerArgument(team = B)
            ),
        ),
        matchArgument(
            description = "valid match with match player goals in favor with maximum amount of 255",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, goalsInFavor = 255),
                matchPlayerArgument(team = B)
            ),
        ),
        matchArgument(
            description = "valid match with match player goals against with minimum amount of 1",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, goalsAgainst = 1),
                matchPlayerArgument(team = B)
            ),
        ),
        matchArgument(
            description = "valid match with match player goals against with maximum amount of 9",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, goalsAgainst = 9),
                matchPlayerArgument(team = B)
            ),
        ),
    )
}