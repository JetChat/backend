package api

import kotlinx.serialization.Serializable

@Serializable
data class CreateUser(
	val username: String,
	val email: String,
	val password: String
)