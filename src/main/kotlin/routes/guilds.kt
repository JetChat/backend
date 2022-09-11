package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.singleOrNull
import sql.models.guild
import sql.runQuery
import utils.getGuildIdParam
import utils.invalidId
import utils.notFound

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
				
				val getGuild = runQuery {
					QueryDsl.from(Meta.guild).where {
						Meta.guild.id eq guildId
					}.singleOrNull()
				} ?: return@get notFound("Guild with id $guildId not found")
				
				call.respond(getGuild)
			}
		}
	}
}