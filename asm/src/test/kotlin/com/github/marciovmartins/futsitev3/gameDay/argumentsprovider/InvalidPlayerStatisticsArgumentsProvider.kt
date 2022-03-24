package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.gameDay.A
import com.github.marciovmartins.futsitev3.gameDay.B
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.playerStatisticDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object InvalidPlayerStatisticsArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // TEAM
        singleMatchArgument(
            testDescription = "invalid game day with invalid empty match player statistics team value",
            playerStatistics = setOf(
                playerStatisticDTO(team = A),
                playerStatisticDTO(team = B),
                playerStatisticDTO(team = ""),
            ),
            expectedExceptions = singleExpectedException(
                message = "Cannot coerce empty String",
                field = "matches[].playerStatistics[].team",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with invalid empty match player statistics team value",
            playerStatistics = setOf(
                playerStatisticDTO(team = A),
                playerStatisticDTO(team = B),
                playerStatisticDTO(team = " "),
            ),
            expectedExceptions = singleExpectedException(
                message = "Cannot coerce empty String",
                field = "matches[].playerStatistics[].team",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with invalid match player statistics team value",
            playerStatistics = setOf(
                playerStatisticDTO(team = A),
                playerStatisticDTO(team = B),
                playerStatisticDTO(team = "C"),
            ),
            expectedExceptions = singleExpectedException(
                message = "\"C\" is not one of the values accepted: [A, B]",
                field = "matches[].playerStatistics[].team",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player statistics team value in smallcase",
            playerStatistics = setOf(
                playerStatisticDTO(team = "a"),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "\"a\" is not one of the values accepted: [A, B]",
                field = "matches[].playerStatistics[].team",
            ),
        ),
        // USER_ID
        singleMatchArgument(
            testDescription = "invalid game day with null match player statistics userId",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, playerId = null),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "cannot be null",
                field = "matches[].playerStatistics[].playerId"
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with invalid match player statistics userId",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, playerId = "invalid-uuid"),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "Invalid uuid format",
                field = "matches[].playerStatistics[].playerId",
            )
        ),
        // GOALS IN FAVOR
        singleMatchArgument(
            testDescription = "invalid game day with negative match player statistics goals in favor",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, goalsInFavor = -1),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].playerStatistics[].goalsInFavor",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player statistics goals in favor value exceeding 9",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, goalsInFavor = 100),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 99",
                field = "matches[].playerStatistics[].goalsInFavor",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player statistics goals in favor",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsInFavor = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].playerStatistics[].goalsInFavor",
//        ),
//        matchArgument( // kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player statistics goals in favor value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsInFavor = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 99",
//            exceptionField = "goalsInFavor",
//        ),

        // GOALS AGAINST
        singleMatchArgument(
            testDescription = "invalid game day with negative match player statistics goals against",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, goalsAgainst = -1),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].playerStatistics[].goalsAgainst",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player statistics goals against value exceeding 9",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, goalsAgainst = 100),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 99",
                field = "matches[].playerStatistics[].goalsAgainst",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player statistics goals against",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsAgainst = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].playerStatistics[].goalsAgainst",
//        ),
//        matchArgument( // kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player statistics goals against value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsAgainst = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 99",
//            exceptionField = "goalsAgainst",
//        ),

        // YELLOW CARDS
        singleMatchArgument(
            testDescription = "invalid game day with negative match player statistics yellow cards",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, yellowCards = -1),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].playerStatistics[].yellowCards",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player statistics yellow cards value exceeding 9",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, yellowCards = 100),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 99",
                field = "matches[].playerStatistics[].yellowCards",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player statistics yellow cards",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, yellowCards = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].playerStatistics[].yellowCards",
//        ),
//        matchArgument( //Kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player statistics yellow cards value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, yellowCards = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 99",
//            exceptionField = "yellowCards",
//        ),

        // BLUE CARDS
        singleMatchArgument(
            testDescription = "invalid game day with negative match player statistics blue cards",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, blueCards = -1),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].playerStatistics[].blueCards",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player statistics blue cards value exceeding 9",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, blueCards = 100),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 99",
                field = "matches[].playerStatistics[].blueCards",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player statistics blue cards",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, blueCards = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].playerStatistics[].blueCards",
//        ),
//        matchArgument( //Kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player statistics blue cards value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, blueCards = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 99",
//            exceptionField = "blueCards",
//        ),

        // RED CARDS
        singleMatchArgument(
            testDescription = "invalid game day with negative match player statistics red cards",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, redCards = -1),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].playerStatistics[].redCards",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player statistics red cards value exceeding 9",
            playerStatistics = setOf(
                playerStatisticDTO(team = A, redCards = 100),
                playerStatisticDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 99",
                field = "matches[].playerStatistics[].redCards",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player statistics red cards",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, redCards = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].playerStatistics[].redCards",
//        ),
//        matchArgument( //Kotlin automatically converts from double to short rounding down
//            description = "invalid game day with match player statistics red cards value as double",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, redCards = 2.9),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "must be less than or equal to 99",
//            exceptionField = "redCards",
//        ),
    )
}