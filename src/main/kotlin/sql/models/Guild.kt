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
import org.komapper.core.dsl.query.single
import org.komapper.core.dsl.query.singleOrNull
import serialization.LocalDateTimeSerializer
import sql.Snowflake
import sql.runQuery
import java.time.LocalDateTime

@Serializable
@KomapperEntity
data class Guild(
	@KomapperId @KomapperColumn("guild_id") val id: Snowflake,
	val name: String,
	val description: String? = null,
	val ownerId: Snowflake,
	@KomapperCreatedAt val createdAt: LocalDateTime = LocalDateTime.now(),
	val iconUrl: String? = null,
)

object GuildController {
	fun create(guild: Guild) = runQuery {
		QueryDsl.insert(Meta.guild).single(guild)
	}
	
	fun get(id: Snowflake) = runQuery {
		QueryDsl.from(Meta.guild).where { Meta.guild.id eq id }.singleOrNull()
	}
}