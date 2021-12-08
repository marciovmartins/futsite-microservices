package com.github.marciovmartins.futsitev3

import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.client.Traverson
import org.springframework.test.web.reactive.server.WebTestClient
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseIT {
    @LocalServerPort
    protected var port = 0

    protected lateinit var webTestClient: WebTestClient

    protected lateinit var traverson: Traverson

    @BeforeEach
    internal fun setUpBase() {
        val baseurl = "http://localhost:$port"
        webTestClient = WebTestClient.bindToServer().baseUrl(baseurl).build()
        traverson = Traverson(URI.create(baseurl), MediaTypes.HAL_JSON)
    }
}