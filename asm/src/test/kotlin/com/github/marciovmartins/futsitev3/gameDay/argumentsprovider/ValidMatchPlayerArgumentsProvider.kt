package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.A
import com.github.marciovmartins.futsitev3.gameDay.B
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.playerStatisticDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object ValidMatchPlayerArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // GOALS IN FAVOR
        gameDayArgument(
            testDescription = "valid match with match player goals in favor with minimum amount of 0",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerStatisticDTO(team = A, goalsInFavor = 0),
                        playerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player goals in favor with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerStatisticDTO(team = A, goalsInFavor = 99),
                        playerStatisticDTO(team = B)
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
                        playerStatisticDTO(team = A, goalsAgainst = 0),
                        playerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player goals against with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerStatisticDTO(team = A, goalsAgainst = 99),
                        playerStatisticDTO(team = B)
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
                        playerStatisticDTO(team = A, yellowCards = 0),
                        playerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player yellow cards with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerStatisticDTO(team = A, yellowCards = 99),
                        playerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player blue cards with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerStatisticDTO(team = A, blueCards = 99),
                        playerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player red cards with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    players = setOf(
                        playerStatisticDTO(team = A, redCards = 99),
                        playerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
    )
}