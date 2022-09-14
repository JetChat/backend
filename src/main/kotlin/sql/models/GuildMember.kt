@file:UseSerializers(LocalDateTimeSerializer::class)
package sql.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperCreatedAt
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.singleOrNull
import serialization.LocalDateTimeSerializer
import sql.Snowflake
import sql.runQuery
import java.time.LocalDateTime

@Serializable
@KomapperEntity
data class GuildMember(
	@KomapperId @KomapperColumn("member_id") val id: Snowflake,
	val nickname: String? = null,
	@KomapperCreatedAt val joinedAt: LocalDateTime = LocalDateTime.now(),
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
		}.singleOrNull()
	}
	
	fun getAll(guildId: Snowflake) = runQuery {
		QueryDsl.from(Meta.guildMember).where {
			Meta.guildMember.guildId eq guildId
		}
	}
}