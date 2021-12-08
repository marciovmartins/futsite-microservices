package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.MyFaker.faker
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.stream.Stream

object ValidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            description = "valid match with minimum data",
            matchDate = LocalDate.now()
        ),
        argument(
            description = "valid match with all data",
            matchDate = LocalDate.now(),
            matchQuote = faker.gameOfThrones().quote(),
            matchAuthor = faker.gameOfThrones().character(),
            matchDescription = faker.lorem().sentence()
        ),
        argument(
            description = "valid match with date in the past",
            matchDate = LocalDate.now().minusDays(1)
        ),
    )
}

object InvalidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            description = "invalid match with date in the future",
            matchDate = LocalDate.now().plusDays(1),
            exceptionMessage = "Date must be today or in the past"
        ),
    )
}

private fun argument(
    description: String,
    matchDate: LocalDate,
    matchQuote: String? = null,
    matchAuthor: String? = null,
    matchDescription: String? = null,
    exceptionMessage: String? = null,
) = Arguments.of(description, matchDate, matchQuote, matchAuthor, matchDescription, exceptionMessage)!!