package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.A
import com.github.marciovmartins.futsitev3.gameDay.B
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.playerDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object InvalidPlayerArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // TEAM
        singleMatchArgument(
            testDescription = "invalid game day with invalid match player team value",
            players = setOf(
                playerDTO(team = A),
                playerDTO(team = B),
                playerDTO(team = "C"),
            ),
            expectedExceptions = singleExpectedException(
                message = "\"C\" is not one of the values accepted: [A, B]",
                field = "matches[].players[].team",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player team value in smallcase",
            players = setOf(
                playerDTO(team = "a"),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "\"a\" is not one of the values accepted: [A, B]",
                field = "matches[].players[].team",
            ),
        ),
        // USER_ID
        singleMatchArgument(
            testDescription = "invalid game day with null match player userId",
            players = setOf(
                playerDTO(team = A, userId = null),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "cannot be null",
                field = "matches[].players[].userId"
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with invalid match player userId",
            players = setOf(
                playerDTO(team = A, userId = "invalid-uuid"),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "Invalid uuid format",
                field = "matches[].players[].userId",
            )
        ),
        // GOALS IN FAVOR
        singleMatchArgument(
            testDescription = "invalid game day with negative match player goals in favor",
            players = setOf(
                playerDTO(team = A, goalsInFavor = -1),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].players[].goalsInFavor",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player goals in favor value exceeding 9",
            players = setOf(
                playerDTO(team = A, goalsInFavor = 10),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].players[].goalsInFavor",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player goals in favor",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsInFavor = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].players[].goalsInFavor",
//        ),
//        matchArgument( // kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player goals in favor value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsInFavor = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 9",
//            exceptionField = "goalsInFavor",
//        ),

        // GOALS AGAINST
        singleMatchArgument(
            testDescription = "invalid game day with negative match player goals against",
            players = setOf(
                playerDTO(team = A, goalsAgainst = -1),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].players[].goalsAgainst",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player goals against value exceeding 9",
            players = setOf(
                playerDTO(team = A, goalsAgainst = 10),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].players[].goalsAgainst",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player goals against",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsAgainst = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].players[].goalsAgainst",
//        ),
//        matchArgument( // kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player goals against value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsAgainst = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 9",
//            exceptionField = "goalsAgainst",
//        ),

        // YELLOW CARDS
        singleMatchArgument(
            testDescription = "invalid game day with negative match player yellow cards",
            players = setOf(
                playerDTO(team = A, yellowCards = -1),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].players[].yellowCards",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player yellow cards value exceeding 9",
            players = setOf(
                playerDTO(team = A, yellowCards = 10),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].players[].yellowCards",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player yellow cards",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, yellowCards = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].players[].yellowCards",
//        ),
//        matchArgument( //Kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player yellow cards value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, yellowCards = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 9",
//            exceptionField = "yellowCards",
//        ),

        // BLUE CARDS
        singleMatchArgument(
            testDescription = "invalid game day with negative match player blue cards",
            players = setOf(
                playerDTO(team = A, blueCards = -1),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].players[].blueCards",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player blue cards value exceeding 9",
            players = setOf(
                playerDTO(team = A, blueCards = 10),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].players[].blueCards",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player blue cards",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, blueCards = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].players[].blueCards",
//        ),
//        matchArgument( //Kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player blue cards value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, blueCards = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 9",
//            exceptionField = "blueCards",
//        ),

        // RED CARDS
        singleMatchArgument(
            testDescription = "invalid game day with negative match player red cards",
            players = setOf(
                playerDTO(team = A, redCards = -1),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].players[].redCards",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player red cards value exceeding 9",
            players = setOf(
                playerDTO(team = A, redCards = 10),
                playerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].players[].redCards",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player red cards",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, redCards = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].players[].redCards",
//        ),
//        matchArgument( //Kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player red cards value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, redCards = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 9",
//            exceptionField = "redCards",
//        ),
    )
}