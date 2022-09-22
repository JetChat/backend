package api

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import sql.Snowflake
import sql.models.GuildMember
import sql.models.User

@Serializable
data class GetGuildMemberPayload(
	val id: Snowflake,
	val nickname: String? = null,
	val joinedAt: Instant,
	val guildId: Snowflake,
	val user: User
) {
	companion object {
		fun fromSQL(member: GuildMember, user: User) = GetGuildMemberPayload(
			id = member.id,
			nickname = member.nickname,
			joinedAt = member.joinedAt,
			guildId = member.guildId,
			user = user
		)
	}
}