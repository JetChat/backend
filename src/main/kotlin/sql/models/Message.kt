@file:UseSerializers(LocalDateTimeSerializer::class)

package sql.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperCreatedAt
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import serialization.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
@KomapperEntity
data class Message(
	@KomapperId @KomapperColumn("message_id") val id: Int,
	val authorId: Int,
	val channelId: Int,
	val content: String?,
	val replyId: Int?,
	@KomapperCreatedAt val createdAt: LocalDateTime,
)