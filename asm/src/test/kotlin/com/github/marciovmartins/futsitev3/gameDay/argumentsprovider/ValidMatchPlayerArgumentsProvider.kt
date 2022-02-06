package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.MyFaker.faker
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchDTO
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchPlayerDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object ValidMatchPlayerArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // NICKNAME
        gameDayArgument(
            testDescription = "valid match with match player nickname with minimum of 1 character",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, nickname = faker.lorem().characters(1)),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player nickname with maximum of 50 character",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, nickname = faker.lorem().characters(50)),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
        // GOALS IN FAVOR
        gameDayArgument(
            testDescription = "valid match with match player goals in favor with minimum amount of 0",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, goalsInFavor = 0),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player goals in favor with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, goalsInFavor = 9),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
        // GOALS AGAINST
        gameDayArgument(
            testDescription = "valid match with match player goals against with minimum amount of 0",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, goalsAgainst = 0),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player goals against with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, goalsAgainst = 9),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
        // YELLOW CARDS
        gameDayArgument(
            testDescription = "valid match with match player yellow cards with minimum amount of 0",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, yellowCards = 0),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player yellow cards with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, yellowCards = 9),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player blue cards with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, blueCards = 9),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
        gameDayArgument(
            testDescription = "valid match with match player red cards with maximum amount of 9",
            matches = setOf(
                matchDTO(
                    matchPlayers = setOf(
                        matchPlayerDTO(team = A, redCards = 9),
                        matchPlayerDTO(team = B)
                    ),
                ),
            ),
        ),
    )
}