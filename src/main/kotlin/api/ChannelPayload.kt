package api

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import sql.Snowflake
import sql.models.Channel
import sql.models.ChannelType

@Serializable
data class ChannelPayload(
	val id: Snowflake,
	val name: String,
	val description: String?,
	val parentId: Snowflake?,
	val guildId: Snowflake,
	val createdAt: Instant,
	val channelType: ChannelType,
	val channelPosition: Int,
	val lastMessageId: Snowflake?,
) {
	companion object {
		fun fromSQL(channel: Channel, lastMessageId: Snowflake?) = ChannelPayload(
			id = channel.id,
			name = channel.name,
			description = channel.description,
			parentId = channel.parentId,
			guildId = channel.guildId,
			createdAt = channel.createdAt,
			channelType = channel.channelType,
			channelPosition = channel.channelPosition,
			lastMessageId = lastMessageId,
		)
	}
}