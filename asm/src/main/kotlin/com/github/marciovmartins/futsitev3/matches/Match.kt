package com.github.marciovmartins.futsitev3.matches

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "matches")
class Match(
    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val internalId: Long? = null,

    @Column(name = "match_id", nullable = false)
    val id: UUID,

    @Column(nullable = false)
    val date: LocalDate,

    @Column(nullable = true)
    val quote: String?,

    @Column(nullable = true)
    val author: String?,

    @Column(nullable = true)
    val description: String?,
)
