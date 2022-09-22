package sql.models

import io.ktor.resources.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.komapper.annotation.EnumType
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperEnum
import org.komapper.annotation.KomapperId
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.singleOrNull
import sql.Snowflake
import sql.runQuery

@Serializable
@KomapperEntity
data class Channel(
	@KomapperId @KomapperColumn("channel_id") val id: Snowflake,
	val name: String,
	val description: String?,
	val parentId: Snowflake?,
	val guildId: Snowflake,
	val createdAt: Instant = Clock.System.now(),
	@KomapperEnum(EnumType.ORDINAL) val channelType: ChannelType,
	val channelPosition: Int,
)

@Serializable
enum class ChannelType {
	@SerialName("0") TEXT
}

object ChannelController {
	fun get(id: Snowflake) = runQuery {
		QueryDsl.from(Meta.channel).where { Meta.channel.id eq id }.singleOrNull()
	}
	
	fun getAll(guidId: Snowflake) = runQuery {
		QueryDsl.from(Meta.channel).where { Meta.channel.guildId eq guidId }
	}
	
	fun getLastMessage(channelId: Snowflake) = runQuery {
		QueryDsl.from(Meta.message).where {
			Meta.message.channelId eq channelId
		}.orderBy(Meta.message.createdAt.desc()).limit(1).singleOrNull()
	}
}