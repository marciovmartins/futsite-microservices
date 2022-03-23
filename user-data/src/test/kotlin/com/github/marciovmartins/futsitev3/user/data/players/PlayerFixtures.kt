package com.github.marciovmartins.futsitev3.user.data.players

import com.github.marciovmartins.futsitev3.user.data.MyFaker
import org.junit.jupiter.params.provider.Arguments
import java.util.UUID

fun createPlayer(
    amateurSoccerGroupId: Any? = UUID.randomUUID().toString(),
    userId: Any? = UUID.randomUUID().toString(),
    nickname: Any? = MyFaker.faker.funnyName().name().take(45)
) = PlayerDTO(amateurSoccerGroupId = amateurSoccerGroupId, userId = userId, nickname = nickname)

fun playerArgument(
    testDescription: String,
    player: PlayerDTO
): Arguments = Arguments.of(testDescription, player)

class PlayerDTO(
    val amateurSoccerGroupId: Any? = null,
    val userId: Any? = null,
    val nickname: Any? = null
)
