package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.BaseIT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.time.LocalDate

class MatchControllerIT : BaseIT() {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidMatchArgumentsProvider::class)
    fun `should succeed to create and retrieve a match`(
        @Suppress("UNUSED_PARAMETER") description: String,
        matchDate: LocalDate,
        matchQuote: String?,
        matchAuthor: String?,
        matchDescription: String?,
    ) {
        // setup
        val matchToCreate = MatchDTO(matchDate, matchQuote, matchAuthor, matchDescription)
        // execution
        val matchLocationUrl = webTestClient.post()
            .uri(traverson.follow("matches").asLink().href)
            .bodyValue(matchToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()
        val match = webTestClient.get()
            .uri(matchLocationUrl)
            .exchange().expectStatus().isOk
            .returnResult(Match::class.java)
            .responseBody.blockFirst()
        // assertion
        assertThat(match).isNotNull
        assertThat(match!!.date).isEqualTo(matchDate)
        assertThat(match.quote).isEqualTo(matchQuote)
        assertThat(match.author).isEqualTo(matchAuthor)
        assertThat(match.description).isEqualTo(matchDescription)
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(InvalidMatchArgumentsProvider::class)
    fun `should fail with invalid data`(
        @Suppress("UNUSED_PARAMETER") description: String,
        matchDate: LocalDate?,
        matchQuote: String?,
        matchAuthor: String?,
        matchDescription: String?,
        exceptionMessage: String,
        exceptionField: String,
    ) {
        // setup
        val matchToCreate = MatchDTO(matchDate, matchQuote, matchAuthor, matchDescription)
        // execution && assertion
        webTestClient.post()
            .uri(traverson.follow("matches").asLink().href)
            .bodyValue(matchToCreate)
            .exchange().expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$[0].message").isEqualTo(exceptionMessage)
            .jsonPath("$[0].field").isEqualTo(exceptionField)
    }
}

private data class MatchDTO(
    val date: LocalDate?,
    val quote: String?,
    val author: String?,
    val description: String?,
)