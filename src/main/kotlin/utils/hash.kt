package utils

import env
import io.ktor.util.*

val digestFunction = getDigestFunction("SHA-256") {
	env("HASH_SECRET", "mysecret")
}

fun hashPassword(password: String) = digestFunction(password).toString()
