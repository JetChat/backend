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
CREATE TABLE dm_channel
(
	dm_channel_id INT UNSIGNED AUTO_INCREMENT
		PRIMARY KEY,
	custom_name   VARCHAR(100) CHARSET utf8 NULL,
	created_at    TIMESTAMP DEFAULT (NOW()) NOT NULL
)
	ENGINE = InnoDB;
 */

@Serializable
@KomapperEntity
data class DmChannel(
	@KomapperId @KomapperColumn("dm_channel_id") val id: Int,
	val customName: String,
	@KomapperCreatedAt val createdAt: LocalDateTime,
)

