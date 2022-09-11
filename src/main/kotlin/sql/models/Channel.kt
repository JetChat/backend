@file:UseSerializers(LocalDateTimeSerializer::class)

package sql.models

import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.komapper.annotation.EnumType
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperEnum
import org.komapper.annotation.KomapperId
import serialization.LocalDateTimeSerializer
import sql.Snowflake
import java.time.LocalDateTime

@Serializable
@KomapperEntity
data class Channel(
	@KomapperId @KomapperColumn("channel_id") val id: Snowflake,
	val name: String,
	val description: String?,
	val parentId: Snowflake?,
	val guildId: Snowflake,
	val createdAt: LocalDateTime,
	@KomapperEnum(EnumType.ORDINAL) val channelType: ChannelType,
	val channelPosition: Int,
)

@Serializable
enum class ChannelType {
	@SerialName("0") TEXT
}