package com.github.marciovmartins.futsitev3.matches

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDate
import java.util.UUID

@Controller
@RequestMapping(
    value = ["/matches"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class MatchController {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(): ResponseEntity<Match> {
        return ResponseEntity.ok(
            Match(
                id = UUID.randomUUID(),
                date = LocalDate.now(),
                quote = null,
                author = null,
                description = null
            )
        )
    }

    @GetMapping("/{matchId}")
    fun findOne(@PathVariable matchId: UUID): ResponseEntity<Match> {
        return ResponseEntity.ok(
            Match(
                id = matchId,
                date = LocalDate.now(),
                quote = null,
                author = null,
                description = null
            )
        )
    }
}