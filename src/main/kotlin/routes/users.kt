package routes

import api.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sql.models.UserController
import utils.getUserIdParam
import utils.invalidId
import utils.notFound

fun Route.users() {
	route("/users") {
		get("{userId}") {
			val userId = getUserIdParam()
			if (userId == -1L) {
				invalidId("user", userId)
				return@get
			}
			
			call.respond(UserController.get(userId) ?: return@get notFound("user", userId))
		}
		
		get("/me") {
			val session = call.principal<UserSession>()!!
			call.respond(UserController.get(session.userId)!!)
		}
	}
}

