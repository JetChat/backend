package utils

import kotlin.random.Random

fun generateRandomDiscriminator(alreadyUsed: List<Int>): String {
	var random = Random.nextInt(1000)
	
	while (alreadyUsed.contains(random)) {
		random = Random.nextInt(1000)
	}
	
	return random.toString().padStart(4, '0')
}