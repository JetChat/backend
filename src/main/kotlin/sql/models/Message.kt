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
CREATE TABLE message
(
	message_id INT UNSIGNED AUTO_INCREMENT
		PRIMARY KEY,
	author_id  INT UNSIGNED               NOT NULL,
	channel_id INT UNSIGNED               NOT NULL,
	content    VARCHAR(4000) CHARSET utf8 NULL,
	reply_id   INT UNSIGNED               NULL,
	created_at TIMESTAMP DEFAULT (NOW())  NOT NULL,
	CONSTRAINT fk_message_channel
		FOREIGN KEY (channel_id) REFERENCES channel (channel_id)
			ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_message_message
		FOREIGN KEY (reply_id) REFERENCES message (message_id),
	CONSTRAINT fk_message_user
		FOREIGN KEY (author_id) REFERENCES user (user_id)
			ON UPDATE CASCADE ON DELETE CASCADE
)
	ENGINE = InnoDB;
 */

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