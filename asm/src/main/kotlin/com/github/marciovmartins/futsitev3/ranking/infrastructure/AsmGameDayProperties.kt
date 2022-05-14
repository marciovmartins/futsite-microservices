package com.github.marciovmartins.futsitev3.ranking.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "asm-gameday")
data class AsmGameDayProperties(
    val baseUrl: String
)
