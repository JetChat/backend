package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.singleOrNull
import sql.models.user
import sql.runQuery
import utils.getUserIdParam
import utils.invalidId
import utils.notFound

fun Route.users() {
	route("/users") {
		get("{userId}") {
			val userId = getUserIdParam()
			if (userId == -1) {
				invalidId("user", userId)
				return@get
			}
			
			val getUser = runQuery {
				QueryDsl.from(Meta.user).where {
					Meta.user.id eq getUserIdParam()
				}.singleOrNull()
			} ?: return@get notFound("User not found")
			
			call.respond(getUser)
		}
	}
}

