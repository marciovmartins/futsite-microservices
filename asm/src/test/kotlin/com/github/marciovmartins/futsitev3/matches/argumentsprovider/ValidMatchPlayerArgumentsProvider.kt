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
                matchPlayerArgument(team = A, faker.lorem().characters(1)),
                matchPlayerArgument(team = B)
            )
        ),
        matchArgument(
            description = "valid match with match player nickname with maximum of 50 character",
            matchDescription = faker.lorem().characters(50),
        ),
    )
}