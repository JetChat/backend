package routes

import api.Channel
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.singleOrNull
import sql.models.channel
import sql.models.message
import sql.runQuery
import sql.runQueryNullable
import utils.getChannelIdParam
import utils.getGuildIdParam
import utils.invalidId
import utils.notFound

fun Route.channels() {
	route("/channels") {
		get {
			val channels = runQuery {
				QueryDsl.from(Meta.channel).where { Meta.channel.guildId eq getGuildIdParam() }
			}
			call.respond(channels)
		}
		route("{channelId}") {
			messages()
			
			get {
				val channelId = getChannelIdParam()
				if (channelId == -1L) {
					invalidId("channel", channelId)
					return@get
				}
				
				val getChannel = runQuery {
					QueryDsl.from(Meta.channel).where {
						Meta.channel.id eq channelId
						Meta.channel.guildId eq getGuildIdParam()
					}.singleOrNull()
				} ?: return@get notFound("channel", channelId)
				
				val lastMessage = runQueryNullable {
					QueryDsl.from(Meta.message).where {
						Meta.message.channelId eq channelId
					}.orderBy(Meta.message.id.desc()).limit(1).singleOrNull()
				}
				
				val apiChannel = Channel.fromSQL(getChannel, lastMessage?.id)
				call.respond(apiChannel)
			}
		}
		
	}
}