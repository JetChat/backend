package utils

import java.security.MessageDigest

fun hashPassword(password: String): String {
	val md = MessageDigest.getInstance("SHA-256")
	val hash = md.digest(password.toByteArray())
	return hash.fold("") { str, it -> str + "%02x".format(it) }
}