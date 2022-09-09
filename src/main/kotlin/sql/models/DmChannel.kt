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
data class DmChannel(
	@KomapperId @KomapperColumn("dm_channel_id") val id: Int,
	val customName: String,
	@KomapperCreatedAt val createdAt: LocalDateTime,
)

