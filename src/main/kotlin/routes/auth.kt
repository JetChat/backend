package routes

import api.Credentials
import api.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import sql.models.UserController
import utils.badRequest
import utils.verifyStringLength
import java.sql.SQLDataException

const val authorisedCharsForUsers = "-_&[]{}.;+!*@#%"

fun Route.auth() {
	get("/login") {
		val credentials = call.receive<Credentials>()
		
		val user = UserController.getUser(credentials.username, credentials.password, credentials.email)
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
	
	post("/register") {
		val credentials = call.receive<Credentials>()
		
		when {
			credentials.username.isBlank() -> badRequest("Username cannot be empty or only whitespaces.")
			credentials.password.isBlank() -> badRequest("Password cannot be empty or only whitespaces.")
			credentials.email.isBlank() -> badRequest("Email cannot be empty or only whitespaces.")
			
			credentials.username.matches(Regex(
				"[^a-z0-9${Regex.escape(authorisedCharsForUsers)}]"
				, RegexOption.IGNORE_CASE)
			) -> badRequest("Username can only contain letters, numbers, and the following symbols: $authorisedCharsForUsers")
			
			!credentials.email.matches(Regex(
				"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
				RegexOption.IGNORE_CASE
			)) -> badRequest("Invalid email pattern.")
			
			else -> {
				verifyStringLength(credentials.username, 2..40, "Username")
				verifyStringLength(credentials.password, 5..100, "Password")
				verifyStringLength(credentials.email, 5 until 512, "Email")
				
				UserController.getUser(credentials.username, credentials.password, credentials.email)?.let {
					call.respond(HttpStatusCode.Conflict)
					return@post
				}
				
				try {
					UserController.createUser(credentials.username, credentials.password, credentials.email)
				} catch (e: SQLDataException) {
					badRequest(e.message ?: "Unknown error.")
					return@post
				}
				call.respond(HttpStatusCode.OK)
			}
		}
	}
}