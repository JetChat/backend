package sql.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import sql.Snowflake

@Serializable
@KomapperEntity
data class DmChannel(
	@KomapperId @KomapperColumn("dm_channel_id") val id: Snowflake,
	val customName: String,
	val createdAt: Instant = Clock.System.now(),
)

