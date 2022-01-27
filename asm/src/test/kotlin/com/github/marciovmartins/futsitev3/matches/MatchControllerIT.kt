package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.BaseIT
import com.github.marciovmartins.futsitev3.matches.MatchFixture.minimumMatchDTO
import com.github.marciovmartins.futsitev3.matches.argumentsprovider.InvalidMatchArgumentsProvider
import com.github.marciovmartins.futsitev3.matches.argumentsprovider.InvalidMatchPlayerArgumentsProvider
import com.github.marciovmartins.futsitev3.matches.argumentsprovider.MatchDTO
import com.github.marciovmartins.futsitev3.matches.argumentsprovider.ValidMatchArgumentsProvider
import com.github.marciovmartins.futsitev3.matches.argumentsprovider.ValidMatchPlayerArgumentsProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class MatchControllerIT : BaseIT() {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidMatchArgumentsProvider::class)
    @ArgumentsSource(ValidMatchPlayerArgumentsProvider::class)
    fun `create and retrieve a match`(
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
    @ArgumentsSource(InvalidMatchPlayerArgumentsProvider::class)
    fun `create match with invalid data fails`(
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

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidMatchArgumentsProvider::class)
    @ArgumentsSource(ValidMatchPlayerArgumentsProvider::class)
    fun `update and retrieve a match`(
        @Suppress("UNUSED_PARAMETER") description: String,
        matchToUpdate: MatchDTO,
    ) {
        // setup
        val matchToCreate = minimumMatchDTO()
        val matchLocationUrl = webTestClient.post()
            .uri(traverson.follow("matches").asLink().href)
            .bodyValue(matchToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()

        // execution
        webTestClient.put()
            .uri(matchLocationUrl)
            .bodyValue(matchToUpdate)
            .exchange().expectStatus().isOk
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
            .isEqualTo(matchToUpdate)
    }
}
