package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.A
import com.github.marciovmartins.futsitev3.gameDay.B
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.playerDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.UUID
import java.util.stream.Stream

object ValidMatchPlayerArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // NICKNAME
        gameDayArgument(
            testDescription = "valid match with match player userId with valid uuid",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerDTO(team = A, userId = UUID.randomUUID().toString()),
                        playerDTO(team = B)
                    ),
                ),
            ),
        ),
        // GOALS IN FAVOR
        gameDayArgument(
            testDescription = "valid match with match player goals in favor with minimum amount of 0",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerDTO(team = A, goalsInFavor = 0),
                        playerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player goals in favor with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerDTO(team = A, goalsInFavor = 9),
                        playerDTO(team = B)
                    ),
                ),
            ),
        ),
        // GOALS AGAINST
        gameDayArgument(
            testDescription = "valid match with match player goals against with minimum amount of 0",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerDTO(team = A, goalsAgainst = 0),
                        playerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player goals against with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerDTO(team = A, goalsAgainst = 9),
                        playerDTO(team = B)
                    ),
                ),
            ),
        ),
        // YELLOW CARDS
        gameDayArgument(
            testDescription = "valid match with match player yellow cards with minimum amount of 0",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerDTO(team = A, yellowCards = 0),
                        playerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player yellow cards with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerDTO(team = A, yellowCards = 9),
                        playerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player blue cards with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerDTO(team = A, blueCards = 9),
                        playerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player red cards with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerDTO(team = A, redCards = 9),
                        playerDTO(team = B)
                    ),
                ),
            ),
        ),
    )
}