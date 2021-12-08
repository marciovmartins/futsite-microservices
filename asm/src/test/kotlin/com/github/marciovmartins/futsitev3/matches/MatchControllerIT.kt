package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.BaseIT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.time.LocalDate

class MatchControllerIT : BaseIT() {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidMatchArgumentsProvider::class)
    fun `should succeed to create and retrieve a match`(
        @Suppress("UNUSED_PARAMETER") description: String,
        date: LocalDate,
        quote: String?,
        author: String?,
        matchDescription: String?,
    ) {
        // setup
        val matchToCreate = Match(
            date = date,
            quote = quote,
            author = author,
            description = matchDescription
        )
        // execution
        val matchLocationUrl = webTestClient.post()
            .uri(traverson.follow("matches").asLink().href)
            .bodyValue(matchToCreate)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()
        val match = webTestClient.get()
            .uri(matchLocationUrl)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange().expectStatus().isOk
            .returnResult(Match::class.java)
            .responseBody.blockFirst()
        // assertion
        assertThat(match).isNotNull
        assertThat(match!!.date).isEqualTo(date)
        assertThat(match.quote).isEqualTo(quote)
        assertThat(match.author).isEqualTo(author)
        assertThat(match.description).isEqualTo(matchDescription)
    }
}