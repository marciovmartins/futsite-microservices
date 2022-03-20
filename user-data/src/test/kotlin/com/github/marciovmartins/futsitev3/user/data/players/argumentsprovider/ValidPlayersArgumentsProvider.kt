package com.github.marciovmartins.futsitev3.user.data.players.argumentsprovider

import com.github.marciovmartins.futsitev3.user.data.MyFaker.faker
import com.github.marciovmartins.futsitev3.user.data.players.createPlayer
import com.github.marciovmartins.futsitev3.user.data.players.playerArgument
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

class ValidPlayersArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        playerArgument(
            testDescription = "valid player with nickname with null value",
            player = createPlayer(nickname = null),
        ),
        playerArgument(
            testDescription = "valid player with nickname with empty value",
            player = createPlayer(nickname = ""),
        ),
        playerArgument(
            testDescription = "valid player with nickname with blank value",
            player = createPlayer(nickname = " ".repeat(45)),
        ),
        playerArgument(
            testDescription = "valid player with nickname with maximum of 45 characters",
            player = createPlayer(nickname = faker.lorem().characters(45)),
        ),
    )
}
