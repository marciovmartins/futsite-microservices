package com.github.marciovmartins.futsitev3.user.data.gameDay

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Suppress("unused")
@Entity(name = "gameDays")
class GameDay(
    @Id
    @Type(type = "uuid-char")
    @Column(length = 36, unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var id: UUID? = null,

    var quote: String?,

    var author: String?,

    @Column(name = "game_day_description")
    var description: String?,
)
