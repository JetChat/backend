package sql.models

import api.GetMessagePayload
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.flatZip
import org.komapper.core.dsl.query.map
import org.komapper.core.dsl.query.single
import org.komapper.core.dsl.query.singleOrNull
import org.komapper.core.dsl.query.zip
import sql.Snowflake
import sql.runQuery
import sql.toInstant
import utils.generateId

@Serializable
@KomapperEntity
data class Message(
	@KomapperId @KomapperColumn("message_id") val id: Snowflake,
	val authorId: Snowflake,
	val channelId: Snowflake,
	val content: String? = null,
	val replyId: Snowflake? = null,
	val createdAt: Instant = Clock.System.now(),
)

object MessageController {
	fun create(channelId: Snowflake, authorId: Snowflake, content: String? = null, replyId: Snowflake? = null) = runQuery {
		QueryDsl.insert(Meta.message).single(
			Message(
				id = generateId(),
				authorId = authorId,
				channelId = channelId,
				content = content,
				replyId = replyId,
			)
		).zip(QueryDsl.from(Meta.user).where {
			Meta.user.id eq authorId
		}.single()).map { (message, user) ->
			GetMessagePayload.fromSQL(message, user)
		}
	}
	
	fun delete(messageId: Snowflake) = runQuery {
		QueryDsl.delete(Meta.message).where {
			Meta.message.id eq messageId
		}
	}
	
	fun has(id: Snowflake) = get(id) != null
	
	fun get(id: Snowflake) = runQuery {
		QueryDsl.from(Meta.message).where {
			Meta.message.id eq id
		}.singleOrNull()
	}
	
	fun get(channelId: Snowflake, limit: Int, before: Snowflake? = null, after: Snowflake? = null) = runQuery {
		QueryDsl.from(Meta.message).where {
			Meta.message.channelId eq channelId
		}.where {
			when {
				after != null -> Meta.message.createdAt greater after.toInstant()
				before != null -> Meta.message.createdAt less before.toInstant()
			}
		}.orderBy(Meta.message.id.desc()).limit(limit).flatZip { messages ->
			QueryDsl.from(Meta.user).where {
				Meta.user.id inList messages.map { it.authorId }
			}
		}.map { (messages, users) ->
			messages.map { message ->
				GetMessagePayload.fromSQL(message, users.first { it.id == message.authorId })
			}
		}
	}
	
	fun getWithAuthor(id: Snowflake) = runQuery {
		QueryDsl.from(Meta.message).where {
			Meta.message.id eq id
		}.singleOrNull().flatZip {
			QueryDsl.from(Meta.user).where {
				Meta.user.id eq it?.authorId
			}.singleOrNull()
		}.map { (message, user) ->
			if (message == null || user == null) null
			else GetMessagePayload.fromSQL(message, user)
		}
	}
}