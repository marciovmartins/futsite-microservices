package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.BaseIT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import reactor.core.publisher.Mono

class MatchControllerIT : BaseIT() {
    @Test
    fun `should succeed to create and retrieve a match`() {
        // setup
        val matchToCreate = MatchFixture.match()
        // execution
        val matchesUrl = traverson.follow("matches").asLink().href
        val matchLocationUrl = webClient.post()
            .uri(matchesUrl)
            .bodyValue(matchToCreate)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchangeToMono { response -> Mono.just(response.headers().header("Location").first()) }
            .block() ?: throw IllegalStateException()
        val match = webClient.get()
            .uri(matchLocationUrl)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve().bodyToMono(Match::class.java)
            .block() ?: throw IllegalStateException()
        // assertion
        assertThat(matchToCreate)
            .usingRecursiveComparison()
            .ignoringFields("internalId")
            .isEqualTo(match)
    }
}