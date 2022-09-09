package sql.models

import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId

/*
CREATE TABLE member_channel
(
	member_id  INT UNSIGNED NOT NULL,
	channel_id INT UNSIGNED NOT NULL,
	CONSTRAINT unq_member_channel
		UNIQUE (member_id, channel_id),
	CONSTRAINT fk_member_channel_channel
		FOREIGN KEY (channel_id) REFERENCES channel (channel_id)
			ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_member_channel_user
		FOREIGN KEY (member_id) REFERENCES member (member_id)
			ON UPDATE CASCADE ON DELETE CASCADE
)
	ENGINE = InnoDB;
 */

@KomapperEntity
data class MemberChannel(
	@KomapperId val memberId: Int,
	@KomapperId val channelId: Int,
)