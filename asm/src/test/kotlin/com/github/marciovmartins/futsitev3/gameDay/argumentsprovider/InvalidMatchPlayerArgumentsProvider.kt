package com.github.marciovmartins.futsitev3.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.MyFaker.faker
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.matchPlayerDTO
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

object InvalidMatchPlayerArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        // TEAM
        singleMatchArgument(
            testDescription = "invalid game day with invalid match player team value",
            matchPlayers = setOf(
                matchPlayerDTO(team = A),
                matchPlayerDTO(team = B),
                matchPlayerDTO(team = "C"),
            ),
            expectedExceptions = singleExpectedException(
                message = "\"C\" is not one of the values accepted: [A, B]",
                field = "matches[].matchPlayers[].team",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player team value in smallcase",
            matchPlayers = setOf(
                matchPlayerDTO(team = "a"),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "\"a\" is not one of the values accepted: [A, B]",
                field = "matches[].matchPlayers[].team",
            ),
        ),
        // NICKNAME
        singleMatchArgument(
            testDescription = "invalid game day with null match player nickname",
            matchPlayers = setOf(
                matchPlayerDTO(team = A, nickname = null),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "cannot be null",
                field = "matches[].matchPlayers[].nickname"
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with blank match player nickname",
            matchPlayers = setOf(
                matchPlayerDTO(team = A, nickname = "     "),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must not be blank",
                field = "matches[].matchPlayers[].nickname",
            )
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player nickname exceeding 50 characters",
            matchPlayers = setOf(
                matchPlayerDTO(team = A, nickname = faker.lorem().characters(51)),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "size must be between 1 and 50",
                field = "matches[].matchPlayers[].nickname",
            ),
        ),
        // GOALS IN FAVOR
        singleMatchArgument(
            testDescription = "invalid game day with negative match player goals in favor",
            matchPlayers = setOf(
                matchPlayerDTO(team = A, goalsInFavor = -1),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].matchPlayers[].goalsInFavor",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player goals in favor value exceeding 9",
            matchPlayers = setOf(
                matchPlayerDTO(team = A, goalsInFavor = 10),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].matchPlayers[].goalsInFavor",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player goals in favor",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsInFavor = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].matchPlayers[].goalsInFavor",
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
            matchPlayers = setOf(
                matchPlayerDTO(team = A, goalsAgainst = -1),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].matchPlayers[].goalsAgainst",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player goals against value exceeding 9",
            matchPlayers = setOf(
                matchPlayerDTO(team = A, goalsAgainst = 10),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].matchPlayers[].goalsAgainst",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player goals against",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, goalsAgainst = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].matchPlayers[].goalsAgainst",
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
            matchPlayers = setOf(
                matchPlayerDTO(team = A, yellowCards = -1),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].matchPlayers[].yellowCards",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player yellow cards value exceeding 9",
            matchPlayers = setOf(
                matchPlayerDTO(team = A, yellowCards = 10),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].matchPlayers[].yellowCards",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player yellow cards",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, yellowCards = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].matchPlayers[].yellowCards",
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
            matchPlayers = setOf(
                matchPlayerDTO(team = A, blueCards = -1),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].matchPlayers[].blueCards",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player blue cards value exceeding 9",
            matchPlayers = setOf(
                matchPlayerDTO(team = A, blueCards = 10),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].matchPlayers[].blueCards",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player blue cards",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, blueCards = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].matchPlayers[].blueCards",
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
            matchPlayers = setOf(
                matchPlayerDTO(team = A, redCards = -1),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be greater than or equal to 0",
                field = "matches[].matchPlayers[].redCards",
            ),
        ),
        singleMatchArgument(
            testDescription = "invalid game day with match player red cards value exceeding 9",
            matchPlayers = setOf(
                matchPlayerDTO(team = A, redCards = 10),
                matchPlayerDTO(team = B),
            ),
            expectedExceptions = singleExpectedException(
                message = "must be less than or equal to 9",
                field = "matches[].matchPlayers[].redCards",
            ),
        ),
//        matchArgument( // https://stackoverflow.com/questions/49900920/kotlin-can-i-force-not-nullable-long-to-be-represented-as-non-primitive-type-in
//            description = "invalid game day with null match player red cards",
//            matchPlayers = setOf(
//                matchPlayerArgument(team = A, redCards = null),
//                matchPlayerArgument(team = B),
//            ),
//            exceptionMessage = "cannot be null",
//            exceptionField = "matches[].matchPlayers[].redCards",
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