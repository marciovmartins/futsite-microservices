package com.github.marciovmartins.futsitev3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HelloWorldServiceTest {
    @Test
    fun `is called without name should return hello world with John Doe name`() {
        // setup
        val name = "John Doe"
        val expected = "Hello World, $name!"
        val helloWorldService = HelloWorldService()
        // execution
        val result = helloWorldService.call()
        // assertions
        assertThat(result).isEqualTo(HelloWorld(expected, name))
    }

    @Test
    fun `is called with name should return hello world with the provided name`() {
        // setup
        val name = "Marcio"
        val expected = "Hello World, $name!"
        val helloWorldService = HelloWorldService()
        // execution
        val result = helloWorldService.call(name)
        // assertions
        assertThat(result).isEqualTo(HelloWorld(expected, name))
    }
}