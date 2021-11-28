package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.MyFaker.faker
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.stream.Stream

object ValidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            argument(
                description = "minimum valid match",
                date = LocalDate.now().plusDays(7)
            ),
            argument(
                description = "maximum valid match",
                date = LocalDate.now().plusDays(7),
                quote = faker.gameOfThrones().quote(),
                author = faker.gameOfThrones().character(),
                matchDescription = faker.lorem().sentence()
            )
        )
    }
}

fun argument(
    description: String,
    date: LocalDate,
    quote: String? = null,
    author: String? = null,
    matchDescription: String? = null
) = Arguments.of(description, date, quote, author, matchDescription)!!