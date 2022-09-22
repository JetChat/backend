package sql.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperIgnore
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.map
import org.komapper.core.dsl.query.singleOrNull
import sql.Snowflake
import sql.runQuery

@Serializable
@KomapperEntity
data class Guild(
	@KomapperId @KomapperColumn("guild_id") val id: Snowflake,
	val name: String,
	val description: String? = null,
	val ownerId: Snowflake,
	val createdAt: Instant = Clock.System.now(),
	val iconUrl: String? = null,
	@KomapperIgnore var members: List<GuildMember> = emptyList(),
	@KomapperIgnore var channels: List<Channel> = emptyList(),
)

object GuildController {
	fun create(guild: Guild) = runQuery {
		QueryDsl.insert(Meta.guild).single(guild)
	}
	
	fun get(id: Snowflake, channels: Boolean = false, members: Boolean = false) = runQuery {
		QueryDsl.from(Meta.guild).where {
			Meta.guild.id eq id
		}.singleOrNull().map {
			if (channels) it?.channels = ChannelController.getAll(id)
			if (members) it?.members = GuildMemberController.getAll(id)
			it
		}
	}
	
	fun getAll(userId: Snowflake) = runQuery {
		QueryDsl.from(Meta.guild).innerJoin(Meta.guildMember) {
			Meta.guild.id eq Meta.guildMember.guildId
		}.where {
			Meta.guildMember.id eq userId
		}
	}
}