package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sql.models.GuildMemberController
import utils.getGuildIdParam
import utils.getMemberIdParam
import utils.invalidId
import utils.notFound

fun Route.members() {
	route("members") {
		get {
			val guildId = getGuildIdParam()
			val getMembers = GuildMemberController.getAll(guildId)
			
			call.respond(getMembers)
		}
		
		route("{memberId}") {
			get {
				val memberId = getMemberIdParam()
				if (memberId == -1L) {
					invalidId("member", memberId)
					return@get
				}
				
				val guildId = getGuildIdParam()
				val getMember = GuildMemberController.get(guildId, memberId) ?: return@get notFound("member", memberId)
				
				call.respond(getMember)
			}
		}
	}
}