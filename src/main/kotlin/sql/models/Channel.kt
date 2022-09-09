@file:UseSerializers(LocalDateTimeSerializer::class)

package sql.models

import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.komapper.annotation.EnumType
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperEnum
import org.komapper.annotation.KomapperId
import serialization.LocalDateTimeSerializer
import java.time.LocalDateTime

/*
CREATE TABLE channel
(
	channel_id       INT UNSIGNED AUTO_INCREMENT
		PRIMARY KEY,
	name             VARCHAR(100) CHARSET utf8 NOT NULL,
	description      VARCHAR(511) CHARSET utf8 NULL,
	parent_id        INT UNSIGNED              NULL,
	guild_id         INT UNSIGNED              NOT NULL,
	created_at       TIMESTAMP DEFAULT (NOW()) NOT NULL,
	channel_type         ENUM('0')  NOT NULL    ,
	channel_position INT UNSIGNED              NOT NULL,
	CONSTRAINT unq_channel
		UNIQUE (channel_position),
	CONSTRAINT fk_channel_channel
		FOREIGN KEY (parent_id) REFERENCES channel (channel_id)
			ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT fk_channel_guild
		FOREIGN KEY (guild_id) REFERENCES guild (guild_id)
			ON UPDATE CASCADE ON DELETE CASCADE
)
	ENGINE = InnoDB;
 */

@Serializable
@KomapperEntity
data class Channel(
	@KomapperId @KomapperColumn("channel_id") val id: Int,
	val name: String,
	val description: String?,
	val parentId: Int?,
	val guildId: Int,
	val createdAt: LocalDateTime,
	@KomapperEnum(EnumType.ORDINAL) val channelType: ChannelType,
	val channelPosition: Int,
)

//@Serializable(with = EnumOrdinalSerializer::class)
@Serializable
enum class ChannelType {
	@SerialName("0")
	TEXT
}