package com.github.marciovmartins.futsitev3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AsmApplicationIT(
    @Autowired val restTemplate: TestRestTemplate
) {
    @Test
    fun `hello world with name`() {
        val name = "Marcio"
        val entity = restTemplate.getForEntity<HelloWorld>("/?name=$name")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body)
            .hasFieldOrPropertyWithValue("message", "Hello World, $name!")
            .hasFieldOrPropertyWithValue("name", name)
    }
}
