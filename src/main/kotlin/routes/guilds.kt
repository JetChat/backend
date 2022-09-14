package routes

import api.GuildPayload
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import logger
import sql.models.Guild
import sql.models.GuildController
import sql.models.GuildMemberController
import sql.models.UserController
import utils.generateId
import utils.getGuildIdParam
import utils.invalidId
import utils.notFound
import utils.verifyStringLength

fun Route.guilds() {
	route("/guilds") {
		route("{guildId}") {
			members()
			channels()
			
			get {
				val guildId = getGuildIdParam()
				if (guildId == -1L) {
					invalidId("guild", guildId)
					return@get
				}
				
				val getMembers = call.request.queryParameters["members"]?.toBoolean() ?: false
				val getGuild = GuildController.get(guildId, getMembers) ?: return@get notFound("guild", guildId)
				call.respond(getGuild)
			}
		}
		
		post("/create") {
			val guildPayload = call.receive<GuildPayload>()
			
			verifyStringLength(guildPayload.name, 1..100, "name")
			verifyStringLength(guildPayload.iconUrl, 1..255, "iconUrl")
			verifyStringLength(guildPayload.description, 1..511, "description")
			
			if (!UserController.has(guildPayload.ownerId)) {
				notFound("user", guildPayload.ownerId)
				return@post
			}
			
			val guild = Guild(
				id = generateId(),
				name = guildPayload.name,
				description = guildPayload.description,
				ownerId = guildPayload.ownerId,
				iconUrl = guildPayload.iconUrl,
			)
			
			val member = GuildMemberController.create(guild.id, guild.ownerId)
			guild.members += member
			call.respond(GuildController.create(guild))
		}
	}
}