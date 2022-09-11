package sql.models

import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import sql.Snowflake

@KomapperEntity
data class MemberChannel(
	@KomapperId val memberId: Snowflake,
	@KomapperId val channelId: Snowflake,
)