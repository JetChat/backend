package api

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import sql.Snowflake

@Serializable
data class CredentialsPayload(
	val username: String = "",
	val email: String = "",
	val password: String = ""
)

data class UserSession(
	val username: String,
	val userId: Snowflake,
	val discriminator: Int,
	val loggedIn: Boolean = false
) : Principal