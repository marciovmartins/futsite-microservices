package com.github.marciovmartins.futsitev3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HelloWorldServiceTest {
    @Test
    fun `is called should return hello world string`() {
        // setup
        val name = "Marcio"
        val expected = "Hello World, $name!"
        val helloWorldService = HelloWorldService()
        // execution
        val result = helloWorldService.call(name)
        // assertions
        assertThat(result).isEqualTo(expected)
    }
}