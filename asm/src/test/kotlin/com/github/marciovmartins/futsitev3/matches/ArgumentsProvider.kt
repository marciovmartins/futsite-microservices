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
            description = "valid match with description with maximum of 2048 character",
            matchDescription = faker.lorem().characters(2048),
        ),
        matchArgument(
            description = "valid match with match player nickname with minimum of 1 character",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, faker.lorem().characters(1)),
                matchPlayerArgument(team = B)
            )
        ),
        matchArgument(
            description = "valid match with match player nickname with maximum of 50 character",
            matchDescription = faker.lorem().characters(50),
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
            description = "invalid match with quote exceeding 255 characters",
            matchQuote = faker.lorem().characters(256),
            exceptionMessage = "size must be between 0 and 255",
            exceptionField = "quote",
        ),
        matchArgument(
            description = "invalid match with author exceeding 50 characters",
            matchAuthor = faker.lorem().characters(51),
            exceptionMessage = "size must be between 0 and 50",
            exceptionField = "author",
        ),
        matchArgument(
            description = "invalid match with description exceeding 2048 characters",
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
            description = "invalid match with exactly only one match player of team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = A),
            ),
            exceptionMessage = "must have at least one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with exactly only one match player of team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "must have at least one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with only match players from team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = A),
                matchPlayerArgument(team = A),
            ),
            exceptionMessage = "must have at least one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with only match players from team A",
            matchPlayers = setOf(
                matchPlayerArgument(team = B),
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "must have at least one player for team A and one player for team B",
            exceptionField = "matchPlayers",
        ),
        matchArgument(
            description = "invalid match with invalid match player team value",
            matchPlayers = setOf(
                matchPlayerArgument(team = A),
                matchPlayerArgument(team = "C"),
            ),
            exceptionMessage = "must be one of the values accepted: [A, B]",
            exceptionField = "matchPlayers.1.team",
        ),
        matchArgument(
            description = "invalid match with null match player nickname",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, nickname = null),
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "cannot be null",
            exceptionField = "matchPlayers.0.nickname",
        ),
        matchArgument(
            description = "invalid match with blank match player nickname",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, nickname = "     "),
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "must not be blank",
            exceptionField = "nickname",
        ),
        matchArgument(
            description = "invalid match with match player nickname exceeding 50 characters",
            matchPlayers = setOf(
                matchPlayerArgument(team = A, nickname = faker.lorem().characters(51)),
                matchPlayerArgument(team = B),
            ),
            exceptionMessage = "size must be between 1 and 50",
            exceptionField = "nickname",
        ),
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

private val A = MatchPlayer.Team.A.name
private val B = MatchPlayer.Team.B.name