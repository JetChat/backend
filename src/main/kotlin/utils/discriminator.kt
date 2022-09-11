package utils

import kotlin.random.Random

fun generateRandomDiscriminator() = Random.nextInt(0, 9999).toString().padStart(4, '0')

fun generateRandomDiscriminator(alreadyUsed: List<Int>): String {
	var random = Random.nextInt(0, 9999)
	
	while (alreadyUsed.contains(random)) {
		random = Random.nextInt(0, 9999)
	}
	
	return random.toString().padStart(4, '0')
}