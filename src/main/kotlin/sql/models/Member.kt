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
CREATE TABLE member
(
	member_id INT UNSIGNED              NOT NULL
		PRIMARY KEY,
	nickname  VARCHAR(40) CHARSET utf8  NULL,
	joined_at TIMESTAMP DEFAULT (NOW()) NOT NULL,
	guild_id  INT UNSIGNED              NOT NULL,
	CONSTRAINT fk_member_guild
		FOREIGN KEY (guild_id) REFERENCES guild (guild_id)
			ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT member_id
		FOREIGN KEY (member_id) REFERENCES user (user_id)
			ON UPDATE CASCADE ON DELETE CASCADE
)
	ENGINE = InnoDB;
 */

@Serializable
@KomapperEntity
data class Member(
	@KomapperId @KomapperColumn("member_id") val id: Int,
	val nickname: String?,
	@KomapperCreatedAt val joinedAt: LocalDateTime,
	val guildId: Int,
)
