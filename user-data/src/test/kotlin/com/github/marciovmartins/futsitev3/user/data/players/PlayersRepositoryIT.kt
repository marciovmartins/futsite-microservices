package com.github.marciovmartins.futsitev3.user.data.players

import com.github.marciovmartins.futsitev3.user.data.BaseIT
import com.github.marciovmartins.futsitev3.user.data.players.argumentsprovider.ValidPlayersArgumentsProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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

    @Test
    fun `retrieve players by amateur soccer group id`() {
        // setup
        val amateurSoccerGroupId = UUID.randomUUID().toString()
        val players = (1..5).map {
            val playerId = UUID.randomUUID()
            webTestClient.put()
                .uri("players/$playerId")
                .bodyValue(createPlayer(amateurSoccerGroupId))
                .exchange()
            webTestClient.get()
                .uri("players/$playerId")
                .exchange()
                .returnResult(PlayerDTO::class.java)
                .responseBody.blockFirst()!!
        }

        // execution
        val response = webTestClient.get()
            .uri("players/search/byAmateurSoccerGroupId?amateurSoccerGroupId=$amateurSoccerGroupId")
            .exchange()

        // assertions
        response.expectStatus().isOk
        val retrievedPlayers = response.returnResult(PlayerCollection::class.java).responseBody.blockFirst()!!
        assertThat(retrievedPlayers)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .ignoringFields("_links.self.href")
            .isEqualTo(
                PlayerCollection(
                    _embedded = PlayerCollection.EmbeddedPlayers(players),
                    _links = Links(self = Links.Link("something")),
                    page = Page(size = 20, totalElements = 5, totalPages = 1, number = 0)
                )
            )
    }
}