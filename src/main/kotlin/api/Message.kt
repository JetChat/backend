package api

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import sql.Snowflake
import sql.models.Message
import sql.models.User

@Serializable
data class CreateMessagePayload(
	val content: String?,
	val replyTo: Snowflake? = null
)

@Serializable
data class GetMessagePayload(
	val id: Snowflake,
	val authorId: Snowflake,
	val channelId: Snowflake,
	val content: String?,
	val replyId: Snowflake?,
	val createdAt: Instant,
	val author: User,
) {
	companion object {
		fun fromSQL(message: Message, author: User): GetMessagePayload = GetMessagePayload(
			message.id,
			message.authorId,
			message.channelId,
			message.content,
			message.replyId,
			message.createdAt,
			author
		)
	}
}
