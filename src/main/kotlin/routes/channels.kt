package routes

import api.ChannelPayload
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sql.models.ChannelController
import utils.getChannelIdParam
import utils.getGuildIdParam
import utils.invalidId
import utils.notFound

fun Route.channels() {
	route("/channels") {
		get {
			val channels = ChannelController.getAll(getGuildIdParam())
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
				
				val getChannel = ChannelController.get(channelId) ?: return@get notFound("channel", channelId)
				val lastMessage = ChannelController.getLastMessage(channelId)
				val apiChannel = ChannelPayload.fromSQL(getChannel, lastMessage?.id)
				call.respond(apiChannel)
			}
		}
	}
}