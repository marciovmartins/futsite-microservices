package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.MyFaker.faker
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.LocalDate
import java.util.stream.Stream

object ValidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        matchArgument(
            description = "valid match with minimum data",
        ),
        matchArgument(
            description = "valid match with all data",
            matchQuote = faker.gameOfThrones().quote(),
            matchAuthor = faker.gameOfThrones().character(),
            matchDescription = faker.lorem().sentence(),
        ),
        matchArgument(
            description = "valid match with date in the past",
            matchDate = LocalDate.now().minusDays(1),
        ),
        matchArgument(
            description = "valid match with quote with minimum of 1 character",
            matchQuote = faker.lorem().characters(1),
        ),
        matchArgument(
            description = "valid match with quote with maximum of 255 character",
            matchQuote = faker.lorem().characters(255),
        ),
        matchArgument(
            description = "valid match with author with minimum of 1 character",
            matchAuthor = faker.lorem().characters(1),
        ),
        matchArgument(
            description = "valid match with author with maximum of 50 character",
            matchAuthor = faker.lorem().characters(50),
        ),
        matchArgument(
            description = "valid match with description with minimum of 1 character",
            matchDescription = faker.lorem().characters(1),
        ),
        matchArgument(
            description = "valid match with description with maximum of 50 character",
            matchDescription = faker.lorem().characters(2048),
        ),
    )
}

object InvalidMatchArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        matchArgument(
            description = "invalid match with date in the future",
            matchDate = LocalDate.now().plusDays(1),
            exceptionMessage = "must be a date in the past or in the present",
            exceptionField = "date",
        ),
        matchArgument(
            description = "invalid match with null date",
            matchDate = null,
            exceptionMessage = "cannot be null",
            exceptionField = "date",
        ),
        matchArgument(
            description = "invalid match with quote with more than 255 characters",
            matchQuote = faker.lorem().characters(256),
            exceptionMessage = "size must be between 0 and 255",
            exceptionField = "quote",
        ),
        matchArgument(
            description = "invalid match with author with more than 50 characters",
            matchAuthor = faker.lorem().characters(51),
            exceptionMessage = "size must be between 0 and 50",
            exceptionField = "author",
        ),
        matchArgument(
            description = "invalid match with description with more than 2048 characters",
            matchDescription = faker.lorem().characters(2049),
            exceptionMessage = "size must be between 0 and 2048",
            exceptionField = "description",
        ),
        matchArgument(
            description = "invalid match with null match players",
            matchPlayers = null,
            exceptionMessage = "cannot be null",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with empty match players",
            matchPlayers = emptySet(),
            exceptionMessage = "must not be empty",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with only one match player of team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = MatchPlayer.Team.A.name)
            ),
            exceptionMessage = "must have one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with only one match player of team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = MatchPlayer.Team.B.name)
            ),
            exceptionMessage = "must have one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        // TODO: only players of team A
        // TODO: only players of team B
        // TODO: invalid team option
    )
}

private fun matchArgument(
    description: String,
    matchDate: LocalDate? = LocalDate.now(),
    matchQuote: String? = null,
    matchAuthor: String? = null,
    matchDescription: String? = null,
    matchPlayers: Set<MatchPlayerDTO>? = setOf(
        matchPlayerArgument(MatchPlayer.Team.A.name),
        matchPlayerArgument(MatchPlayer.Team.B.name),
    ),
    exceptionMessage: String? = null,
    exceptionField: String? = null,
) = Arguments.of(
    description,
    MatchDTO(matchDate, matchQuote, matchAuthor, matchDescription, matchPlayers),
    exceptionMessage,
    exceptionField
)!!

private fun matchPlayerArgument(
    team: String?,
    nickname: String? = faker.superhero().name(),
    goalsInFavor: Int? = faker.random().nextInt(0, 5),
    goalsAgainst: Int? = faker.random().nextInt(0, 1),
    yellowCards: Int? = faker.random().nextInt(0, 2),
    blueCards: Int? = faker.random().nextInt(0, 3),
    redCards: Int? = faker.random().nextInt(0, 1),
) = MatchPlayerDTO(team, nickname, goalsInFavor, goalsAgainst, yellowCards, blueCards, redCards)

data class MatchDTO(
    val date: LocalDate?,
    val quote: String?,
    val author: String?,
    val description: String?,
    val matchPlayers: Set<MatchPlayerDTO>?,
)

data class MatchPlayerDTO(
    val team: String?,
    val nickname: String?,
    val goalsInFavor: Int?,
    val goalsAgainst: Int?,
    val yellowCards: Int?,
    val blueCards: Int?,
    val redCards: Int?,
)