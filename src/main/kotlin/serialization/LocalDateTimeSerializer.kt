package serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
	override val descriptor: SerialDescriptor = String.serializer().descriptor
			
			override
	fun deserialize(decoder: Decoder): LocalDateTime {
		return LocalDateTime.parse(decoder.decodeString())
	}
	
	override fun serialize(encoder: Encoder, value: LocalDateTime) {
		encoder.encodeString(value.toString())
	}
}