package sql.models

import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId

@KomapperEntity
data class MemberChannel(
	@KomapperId val memberId: Int,
	@KomapperId val channelId: Int,
)