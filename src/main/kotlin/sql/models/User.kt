@file:UseSerializers(LocalDateTimeSerializer::class)

package sql.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperCreatedAt
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import serialization.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
@KomapperEntity
data class User(
	@KomapperId @KomapperColumn("user_id") val id: Int,
	val avatarUrl: String?,
	@KomapperCreatedAt val createdAt: LocalDateTime,
	val description: String?,
	val discriminator: Int,
	@Transient val email: String = "",
	@Transient val password: String = "",
	val username: String,
)
