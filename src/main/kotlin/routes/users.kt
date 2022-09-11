package routes

import api.CreateUser
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.map
import org.komapper.core.dsl.query.singleOrNull
import sql.models.User
import sql.models.user
import sql.runQuery
import utils.generateRandomDiscriminator
import utils.getUserIdParam
import utils.hashPassword
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
		
		post("create") {
			val user = call.receive<CreateUser>()
			runQuery {
				QueryDsl.from(Meta.user).where {
					Meta.user.email eq user.email
				}.singleOrNull()
			}?.let {
				call.respond("An account with this email already exists")
				return@post
			}
			
			val alreadyExistingDiscriminators = runQuery {
				QueryDsl.from(Meta.user).where {
					Meta.user.username eq user.username
				}.map {
					it.map(User::discriminator)
				}
			}
			
			val discriminator = generateRandomDiscriminator(alreadyExistingDiscriminators)
			val hashedPassword = hashPassword(user.password)
			
			val createUser = runQuery {
				QueryDsl.insert(Meta.user).values {
					it.username to user.username
					it.email to user.email
					it.password to hashedPassword
					it.discriminator to discriminator
				}
			}
			
			call.respond(createUser)
		}
	}
}

