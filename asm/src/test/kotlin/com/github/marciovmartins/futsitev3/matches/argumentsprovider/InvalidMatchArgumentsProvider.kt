package com.github.marciovmartins.futsitev3.matches.argumentsprovider

import com.github.marciovmartins.futsitev3.MyFaker.faker
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.stream.Stream

object InvalidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        matchArgument(
            description = "invalid match date with invalid date format",
            matchDate = "invalid-format",
            exceptionMessage = "Text 'invalid-format' could not be parsed at index 0",
            exceptionField = "date",
        ),
        matchArgument(
            description = "invalid match date with wrong date in human readable format",
            matchDate = "03/12/2007",
            exceptionMessage = "Text '03/12/2007' could not be parsed at index 0",
            exceptionField = "date",
        ),
        matchArgument(
            description = "invalid match date with wrong date in millis",
            matchDate = "1642941722026",
            exceptionMessage = "Text '1642941722026' could not be parsed at index 0",
            exceptionField = "date",
        ),
        matchArgument(
            description = "invalid match with date in the future",
            matchDate = LocalDate.now().plusDays(1),
            exceptionMessage = "must be a date in the past or in the present",
            exceptionField = "date",
        ),
        matchArgument(
            description = "invalid match with null date",
            matchDate = null,
            exceptionMessage = "cannot be null",
            exceptionField = "date",
        ),
        matchArgument(
            description = "invalid match with quote exceeding 255 characters",
            matchQuote = faker.lorem().characters(256),
            exceptionMessage = "size must be between 0 and 255",
            exceptionField = "quote",
        ),
        matchArgument(
            description = "invalid match with author exceeding 50 characters",
            matchAuthor = faker.lorem().characters(51),
            exceptionMessage = "size must be between 0 and 50",
            exceptionField = "author",
        ),
        matchArgument(
            description = "invalid match with description exceeding 2048 characters",
            matchDescription = faker.lorem().characters(2049),
            exceptionMessage = "size must be between 0 and 2048",
            exceptionField = "description",
        ),
    )
}