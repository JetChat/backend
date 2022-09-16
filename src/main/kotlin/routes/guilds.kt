package routes

import api.CreateGuildPayload
import api.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
		get {
			val session = call.principal<UserSession>()!!
			val guilds = GuildController.getAll(session.userId)
			call.respond(guilds)
		}
		
		route("{guildId}") {
			members()
			channels()
			
			get {
				val guildId = getGuildIdParam()
				if (guildId == -1L) {
					invalidId("guild", guildId)
					return@get
				}
				
				val getChannels = call.request.queryParameters["channels"]?.toBoolean() ?: false
				val getMembers = call.request.queryParameters["members"]?.toBoolean() ?: false
				val getGuild = GuildController.get(guildId, getChannels, getMembers) ?: return@get notFound("guild", guildId)
				call.respond(getGuild)
			}
		}
		
		post("/create") {
			val createGuildPayload = call.receive<CreateGuildPayload>()
			
			verifyStringLength(createGuildPayload.name, 1..100, "name")
			verifyStringLength(createGuildPayload.iconUrl, 1..255, "iconUrl")
			verifyStringLength(createGuildPayload.description, 1..511, "description")
			
			if (!UserController.has(createGuildPayload.ownerId)) {
				notFound("user", createGuildPayload.ownerId)
				return@post
			}
			
			val guild = Guild(
				id = generateId(),
				name = createGuildPayload.name,
				description = createGuildPayload.description,
				ownerId = createGuildPayload.ownerId,
				iconUrl = createGuildPayload.iconUrl,
			)
			
			val member = GuildMemberController.create(guild.id, guild.ownerId)
			guild.members += member
			call.respond(GuildController.create(guild))
		}
	}
}