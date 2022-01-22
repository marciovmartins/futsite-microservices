package com.github.marciovmartins.futsitev3.matches.argumentsprovider

import com.github.marciovmartins.futsitev3.MyFaker.faker
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.stream.Stream

object ValidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        matchArgument(
            description = "valid match with minimum data",
        ),
        matchArgument(
            description = "valid match with all data",
            matchQuote = faker.gameOfThrones().quote(),
            matchAuthor = faker.gameOfThrones().character(),
            matchDescription = faker.lorem().sentence(),
        ),
        matchArgument(
            description = "valid match with date in the past",
            matchDate = LocalDate.now().minusDays(1),
        ),
        matchArgument(
            description = "valid match with quote with minimum of 1 character",
            matchQuote = faker.lorem().characters(1),
        ),
        matchArgument(
            description = "valid match with quote with maximum of 255 character",
            matchQuote = faker.lorem().characters(255),
        ),
        matchArgument(
            description = "valid match with author with minimum of 1 character",
            matchAuthor = faker.lorem().characters(1),
        ),
        matchArgument(
            description = "valid match with author with maximum of 50 character",
            matchAuthor = faker.lorem().characters(50),
        ),
        matchArgument(
            description = "valid match with description with minimum of 1 character",
            matchDescription = faker.lorem().characters(1),
        ),
        matchArgument(
            description = "valid match with description with maximum of 2048 character",
            matchDescription = faker.lorem().characters(2048),
        ),
    )
}