package api

import kotlinx.serialization.Serializable
import sql.Snowflake

@Serializable
data class GuildPayload(
	val name: String,
	val description: String?,
	val ownerId: Snowflake,
	val iconUrl: String? = null
)
