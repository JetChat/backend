package routes

import api.LoginUser
import api.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import sql.models.UserController

fun Route.auth() {
	get("/login") {
		val session = call.receive<LoginUser>()
		
		val user = UserController.getUser(session.username, session.password, session.email)
		if (user == null) {
			call.respond(HttpStatusCode.NotFound)
			return@get
		}
		
		call.sessions.set(UserSession(user.username, user.id, user.discriminator, true))
		call.respond(HttpStatusCode.OK)
	}
	
	post("/logout") {
		call.sessions.clear<UserSession>()
		call.respond(HttpStatusCode.OK)
	}
}