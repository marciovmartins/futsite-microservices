package com.github.marciovmartins.futsitev3.matches

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.time.LocalDate

class MatchTest {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(InvalidMatchArgumentsProvider::class)
    fun `should fail with invalid data`(
        @Suppress("UNUSED_PARAMETER") description: String,
        date: LocalDate,
        quote: String?,
        author: String?,
        matchDescription: String?,
        exceptionMessage: String,
    ) {
        // execution && assertion
        assertThatThrownBy {
            Match(
                date = date,
                quote = quote,
                author = author,
                description = matchDescription
            )
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage(exceptionMessage)
    }
}