package api

import io.ktor.server.http.*
import kotlinx.serialization.Serializable
import sql.models.Message
import sql.models.User

@Serializable
data class Message(
	val id: Int,
	val authorId: Int,
	val channelId: Int,
	val content: String?,
	val replyId: Int?,
	val createdAt: String,
	val author: User,
) {
	companion object {
		fun fromSQL(message: Message, author: User): api.Message = Message(
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
