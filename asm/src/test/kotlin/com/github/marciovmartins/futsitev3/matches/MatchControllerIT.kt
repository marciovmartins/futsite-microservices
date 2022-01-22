package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.BaseIT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class MatchControllerIT : BaseIT() {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidMatchArgumentsProvider::class)
    fun `should succeed to create and retrieve a match`(
        @Suppress("UNUSED_PARAMETER") description: String,
        matchToCreate: MatchDTO,
    ) {
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
            .returnResult(MatchDTO::class.java)
            .responseBody.blockFirst()

        // assertion
        assertThat(match)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(matchToCreate)
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(InvalidMatchArgumentsProvider::class)
    fun `should fail with invalid data`(
        @Suppress("UNUSED_PARAMETER") description: String,
        matchToCreate: MatchDTO,
        exceptionMessage: String,
        exceptionField: String,
    ) {
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
