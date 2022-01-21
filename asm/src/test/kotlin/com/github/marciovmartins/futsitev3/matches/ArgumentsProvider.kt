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
        ),
        argument(
            description = "valid match with all data",
            matchQuote = faker.gameOfThrones().quote(),
            matchAuthor = faker.gameOfThrones().character(),
            matchDescription = faker.lorem().sentence(),
        ),
        argument(
            description = "valid match with date in the past",
            matchDate = LocalDate.now().minusDays(1),
        ),
        argument(
            description = "valid match with quote with minimum of 1 character",
            matchQuote = faker.lorem().characters(1),
        ),
        argument(
            description = "valid match with quote with maximum of 255 character",
            matchQuote = faker.lorem().characters(255),
        ),
        argument(
            description = "valid match with author with minimum of 1 character",
            matchAuthor = faker.lorem().characters(1),
        ),
        argument(
            description = "valid match with author with maximum of 50 character",
            matchAuthor = faker.lorem().characters(50),
        ),
        argument(
            description = "valid match with description with minimum of 1 character",
            matchDescription = faker.lorem().characters(1),
        ),
        argument(
            description = "valid match with description with maximum of 50 character",
            matchDescription = faker.lorem().characters(2048),
        ),
    )
}

object InvalidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            description = "invalid match with date in the future",
            matchDate = LocalDate.now().plusDays(1),
            exceptionMessage = "must be a date in the past or in the present",
            exceptionField = "date",
        ),
        argument(
            description = "invalid match with null date",
            matchDate = null,
            exceptionMessage = "cannot be null",
            exceptionField = "date",
        ),
        argument(
            description = "invalid match with quote with more than 255 characters",
            matchQuote = faker.lorem().characters(256),
            exceptionMessage = "size must be between 0 and 255",
            exceptionField = "quote",
        ),
        argument(
            description = "invalid match with author with more than 50 characters",
            matchAuthor = faker.lorem().characters(51),
            exceptionMessage = "size must be between 0 and 50",
            exceptionField = "author",
        ),
        argument(
            description = "invalid match with description with more than 2048 characters",
            matchDescription = faker.lorem().characters(2049),
            exceptionMessage = "size must be between 0 and 2048",
            exceptionField = "description",
        ),
    )
}

private fun argument(
    description: String,
    matchDate: LocalDate? = LocalDate.now(),
    matchQuote: String? = null,
    matchAuthor: String? = null,
    matchDescription: String? = null,
    exceptionMessage: String? = null,
    exceptionField: String? = null,
) = Arguments.of(
    description,
    MatchDTO(matchDate, matchQuote, matchAuthor, matchDescription),
    exceptionMessage,
    exceptionField
)!!

data class MatchDTO(
    val date: LocalDate?,
    val quote: String?,
    val author: String?,
    val description: String?,
)