package com.github.marciovmartins.futsitev3.asm.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.asm.gameDay.A
import com.github.marciovmartins.futsitev3.asm.gameDay.B
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayFixture.testMatchDTO
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayFixture.testPlayerStatisticDTO
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
                testMatchDTO(
                    players = setOf(
                        testPlayerStatisticDTO(team = A, goalsInFavor = 0),
                        testPlayerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player goals in favor with maximum amount of 9",
            matches = setOf(
                testMatchDTO(
                    players = setOf(
                        testPlayerStatisticDTO(team = A, goalsInFavor = 99),
                        testPlayerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        // GOALS AGAINST
        gameDayArgument(
            testDescription = "valid match with match player goals against with minimum amount of 0",
            matches = setOf(
                testMatchDTO(
                    players = setOf(
                        testPlayerStatisticDTO(team = A, goalsAgainst = 0),
                        testPlayerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player goals against with maximum amount of 9",
            matches = setOf(
                testMatchDTO(
                    players = setOf(
                        testPlayerStatisticDTO(team = A, goalsAgainst = 99),
                        testPlayerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        // YELLOW CARDS
        gameDayArgument(
            testDescription = "valid match with match player yellow cards with minimum amount of 0",
            matches = setOf(
                testMatchDTO(
                    players = setOf(
                        testPlayerStatisticDTO(team = A, yellowCards = 0),
                        testPlayerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player yellow cards with maximum amount of 9",
            matches = setOf(
                testMatchDTO(
                    players = setOf(
                        testPlayerStatisticDTO(team = A, yellowCards = 99),
                        testPlayerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player blue cards with maximum amount of 9",
            matches = setOf(
                testMatchDTO(
                    players = setOf(
                        testPlayerStatisticDTO(team = A, blueCards = 99),
                        testPlayerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player red cards with maximum amount of 9",
            matches = setOf(
                testMatchDTO(
                    players = setOf(
                        testPlayerStatisticDTO(team = A, redCards = 99),
                        testPlayerStatisticDTO(team = B)
                    ),
                ),
            ),
        ),
    )
}