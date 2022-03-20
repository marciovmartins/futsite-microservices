package com.github.marciovmartins.futsitev3.user.data.players

import com.github.marciovmartins.futsitev3.user.data.BaseIT
import com.github.marciovmartins.futsitev3.user.data.players.argumentsprovider.ValidPlayersArgumentsProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.UUID

class PlayersRepositoryIT : BaseIT() {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidPlayersArgumentsProvider::class)
    fun `create and retrieve player`(
        testDescription: String,
        playerToCreate: PlayerDTO
    ) {
        // setup
        val playerId = UUID.randomUUID()

        // execution
        val insertResponse = webTestClient.put()
            .uri("players/$playerId")
            .bodyValue(playerToCreate)
            .exchange()
        val retrievalResponse = webTestClient.get()
            .uri("players/$playerId")
            .exchange()

        // assertions
        insertResponse.expectStatus().isCreated
        retrievalResponse.expectStatus().isOk

        val player = retrievalResponse.returnResult(PlayerDTO::class.java).responseBody.blockFirst()
        assertThat(player)
            .usingRecursiveComparison()
            .isEqualTo(playerToCreate)
    }
}