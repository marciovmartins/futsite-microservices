package com.github.marciovmartins.futsitev3.matches

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.time.LocalDate

class MatchTest {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidMatchArgumentsProvider::class)
    fun `create valid match`(
        description: String,
        date: LocalDate,
        quote: String?,
        author: String?,
        matchDescription: String?
    ) {
        // execution
        val match = Match(date, quote, author, matchDescription)

        // assertions
        assertThat(match.date).isEqualTo(date)
        assertThat(match.quote).isEqualTo(quote)
        assertThat(match.author).isEqualTo(author)
        assertThat(match.description).isEqualTo(matchDescription)
    }
}