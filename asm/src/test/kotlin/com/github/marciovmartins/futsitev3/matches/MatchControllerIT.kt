package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.BaseIT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class MatchControllerIT : BaseIT() {
    @Test
    fun `should succeed to create and retrieve a match`() {
        // setup
        val matchToCreate = MatchFixture.match()
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
        assertThat(matchToCreate)
            .usingRecursiveComparison()
            .ignoringFields("internalId")
            .isEqualTo(match)
    }
}