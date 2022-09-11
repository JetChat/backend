package api

import io.ktor.server.http.*
import kotlinx.serialization.Serializable
import sql.Snowflake
import sql.models.Message
import sql.models.User

@Serializable
data class CreateMessage(
	val content: String?,
	val replyTo: Snowflake? = null
)

@Serializable
data class GetMessage(
	val id: Snowflake,
	val authorId: Snowflake,
	val channelId: Snowflake,
	val content: String?,
	val replyId: Snowflake?,
	val createdAt: String,
	val author: User,
) {
	companion object {
		fun fromSQL(message: Message, author: User): GetMessage = GetMessage(
			message.id,
			message.authorId,
			message.channelId,
			message.content,
			message.replyId,
			message.createdAt.toHttpDateString(),
			author
		)
	}
}
