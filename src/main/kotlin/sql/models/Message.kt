@file:UseSerializers(LocalDateTimeSerializer::class)

package sql.models

import api.GetMessagePayload
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperCreatedAt
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.core.dsl.query.flatZip
import org.komapper.core.dsl.query.map
import org.komapper.core.dsl.query.single
import org.komapper.core.dsl.query.singleOrNull
import serialization.LocalDateTimeSerializer
import sql.Snowflake
import sql.runQuery
import utils.generateId
import utils.getChannelIdParam
import java.time.LocalDateTime

@Serializable
@KomapperEntity
data class Message(
	@KomapperId @KomapperColumn("message_id") val id: Snowflake,
	val authorId: Snowflake,
	val channelId: Snowflake,
	val content: String? = null,
	val replyId: Snowflake? = null,
	@KomapperCreatedAt val createdAt: LocalDateTime = LocalDateTime.now(),
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
		)
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
				after != null -> Meta.message.id greater after
				else -> Meta.message.id less before
			}
		}.orderBy(Meta.message.id.desc()).limit(limit)
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