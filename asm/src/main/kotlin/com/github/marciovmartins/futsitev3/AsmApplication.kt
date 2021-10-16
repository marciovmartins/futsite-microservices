package com.github.marciovmartins.futsitev3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
class AsmApplication

fun main(args: Array<String>) {
    runApplication<AsmApplication>(*args)
}

@Controller
class HelloWorldController {
    @GetMapping("/")
    fun helloWorld(): ResponseEntity<String> = ResponseEntity.ok("Hello World")
}