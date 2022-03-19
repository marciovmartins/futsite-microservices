package com.github.marciovmartins.futsitev3.user.data.gameDay.argumentsprovider

import com.github.marciovmartins.futsitev3.user.data.MyFaker.faker
import com.github.marciovmartins.futsitev3.user.data.gameDay.gameDayArgument
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

class ValidGameDayArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        gameDayArgument(
            testDescription = "valid game day without any property",
            quote = null,
            author = null,
            description = null,
        ),
        gameDayArgument(
            testDescription = "valid game day with quote only",
            quote = faker.howIMetYourMother().quote().take(255),
            author = null,
            description = null,
        ),
        gameDayArgument(
            testDescription = "valid game day with quote and author only",
            quote = faker.howIMetYourMother().quote().take(255),
            author = faker.howIMetYourMother().character().take(45),
            description = null,
        ),
        gameDayArgument(
            testDescription = "valid game day with description only",
            quote = null,
            author = null,
            description = faker.howIMetYourMother().catchPhrase().take(2048),
        ),
        gameDayArgument(
            testDescription = "valid game day with all properties",
            quote = faker.howIMetYourMother().quote().take(255),
            author = faker.howIMetYourMother().character().take(45),
            description = faker.howIMetYourMother().catchPhrase().take(2048),
        ),
        gameDayArgument(
            testDescription = "valid game day with quote with maximum of 255 characters",
            quote = faker.lorem().characters(255),
        ),
        gameDayArgument(
            testDescription = "valid game day with author with maximum of 45 characters",
            author = faker.lorem().characters(45),
        ),
        gameDayArgument(
            testDescription = "valid game day with description with maximum of 2048 characters",
            description = faker.lorem().characters(2048),
        ),
    )
}
