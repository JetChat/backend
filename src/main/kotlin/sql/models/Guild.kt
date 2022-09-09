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

/*
CREATE TABLE guild
(
	guild_id    INT UNSIGNED AUTO_INCREMENT
		PRIMARY KEY,
	name        VARCHAR(100) CHARSET utf8 NOT NULL,
	description VARCHAR(511) CHARSET utf8 NULL,
	owner_id    INT UNSIGNED              NOT NULL,
	created_at  TIMESTAMP DEFAULT (NOW()) NOT NULL,
	icon_url    VARCHAR(255) CHARSET utf8 NULL,
	CONSTRAINT owner_id
		UNIQUE (owner_id),
	CONSTRAINT fk_guild_guild
		FOREIGN KEY (owner_id) REFERENCES user (user_id)
			ON UPDATE CASCADE ON DELETE CASCADE
)
 */

@Serializable
@KomapperEntity
data class Guild(
	@KomapperId @KomapperColumn("guild_id") val id: Int,
	val name: String,
	val description: String?,
	val ownerId: Int,
	@KomapperCreatedAt val createdAt: LocalDateTime,
	val iconUrl: String?,
)