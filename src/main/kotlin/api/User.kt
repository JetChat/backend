package api

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class CreateUser(
	val username: String,
	val email: String,
	val password: String
)

@Serializable
data class LoginUser(
	val username: String = "",
	val email: String = "",
	val password: String = ""
)

data class UserSession(
	val user: LoginUser,
	val loggedIn: Boolean = false
) : Principal