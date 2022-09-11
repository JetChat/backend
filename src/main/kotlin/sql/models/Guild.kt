@file:UseSerializers(LocalDateTimeSerializer::class)
package sql.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperCreatedAt
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import serialization.LocalDateTimeSerializer
import sql.Snowflake
import java.time.LocalDateTime

@Serializable
@KomapperEntity
data class Guild(
	@KomapperId @KomapperColumn("guild_id") val id: Snowflake,
	val name: String,
	val description: String?,
	val ownerId: Snowflake,
	@KomapperCreatedAt val createdAt: LocalDateTime,
	val iconUrl: String?,
)