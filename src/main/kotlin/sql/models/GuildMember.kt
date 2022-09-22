package sql.models

import api.GetGuildMemberPayload
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.flatMap
import org.komapper.core.dsl.query.flatZip
import org.komapper.core.dsl.query.map
import org.komapper.core.dsl.query.singleOrNull
import sql.Snowflake
import sql.runQuery

@Serializable
@KomapperEntity
data class GuildMember(
	@SerialName("userId")
	@KomapperId
	@KomapperColumn("member_id")
	val id: Snowflake,
	val nickname: String? = null,
	val joinedAt: Instant = Clock.System.now(),
	val guildId: Snowflake,
)

object GuildMemberController {
	fun create(guildId: Snowflake, userId: Snowflake) = runQuery {
		QueryDsl.insert(Meta.guildMember).single(GuildMember(userId, guildId = guildId))
	}
	
	fun get(guildId: Snowflake, userId: Snowflake) = runQuery {
		QueryDsl.from(Meta.guildMember).where {
			Meta.guildMember.guildId eq guildId
			Meta.guildMember.id eq userId
		}.singleOrNull().flatZip {
			QueryDsl.from(Meta.user).where {
				Meta.user.id eq it?.id
			}.singleOrNull()
		}.map { (member, user) ->
			if (member == null || user == null) null
			else GetGuildMemberPayload.fromSQL(member, user)
		}
	}
	
	fun getAll(guildId: Snowflake) = runQuery {
		QueryDsl.from(Meta.guildMember).where {
			Meta.guildMember.guildId eq guildId
		}
	}
	
	fun getAllWithUser(guildId: Snowflake) = runQuery {
		QueryDsl.from(Meta.guildMember).where {
			Meta.guildMember.guildId eq guildId
		}.flatMap { list ->
			QueryDsl.from(Meta.user).where {
				Meta.user.id inList list.map { it.id }
			}.map { users ->
				list.mapNotNull { member ->
					users.find { it.id == member.id }?.let { user ->
						GetGuildMemberPayload.fromSQL(member, user)
					}
				}
			}
		}
	}
}