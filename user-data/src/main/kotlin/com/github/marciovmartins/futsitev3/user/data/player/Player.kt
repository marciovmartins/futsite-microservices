package com.github.marciovmartins.futsitev3.user.data.player

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id

@Suppress("unused")
@Entity(name = "players")
class Player(
    @Id
    @Type(type = "uuid-char")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var id: UUID? = null,

    @Type(type = "uuid-char")
    var amateurSoccerGroupId: UUID,

    @Type(type = "uuid-char")
    var userId: UUID?,

    var nickname: String?,
)