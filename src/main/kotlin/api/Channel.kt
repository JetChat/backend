package api

import io.ktor.server.http.*
import kotlinx.serialization.Serializable
import sql.models.Channel
import sql.models.ChannelType

@Serializable
data class Channel(
	val id: Int,
	val name: String,
	val description: String?,
	val parentId: Int?,
	val guildId: Int,
	val createdAt: String,
	val channelType: ChannelType,
	val channelPosition: Int,
	val lastMessageId: Int?,
) {
	companion object {
		fun fromSQL(channel: Channel, lastMessageId: Int?): api.Channel = Channel(
			id = channel.id,
			name = channel.name,
			description = channel.description,
			parentId = channel.parentId,
			guildId = channel.guildId,
			createdAt = channel.createdAt.toHttpDateString(),
			channelType = channel.channelType,
			channelPosition = channel.channelPosition,
			lastMessageId = lastMessageId,
		)
	}
}