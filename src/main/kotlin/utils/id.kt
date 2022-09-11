package utils

import kotlin.random.Random

fun generateId(): Long {
	val randomInt = Random.nextInt(1000)
	val timestamp = System.currentTimeMillis()
	return (timestamp.toString() + randomInt.toString().padStart(4, '0')).toLong()
}