package api

import io.ktor.server.http.*
import kotlinx.serialization.Serializable
import sql.models.Message
import sql.models.User

@Serializable
data class CreateMessage(
	val text: String,
	val author: String,
	val replyTo: Int? = null
)

@Serializable
data class SentMessage(
	val id: Int,
	val authorId: Int,
	val channelId: Int,
	val content: String?,
	val replyId: Int?,
	val createdAt: String,
	val author: User,
) {
	companion object {
		fun fromSQL(message: Message, author: User): api.SentMessage = SentMessage(
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
