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
            testDescription = "valid game day with quote only",
            quote = faker.howIMetYourMother().quote(),
        ),
        gameDayArgument(
            testDescription = "valid game day with quote and author only",
            quote = faker.howIMetYourMother().quote(),
            author = faker.howIMetYourMother().character()
        ),
        gameDayArgument(
            testDescription = "valid game day with description only",
            description = faker.howIMetYourMother().catchPhrase(),
        ),
    )
}
