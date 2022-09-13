@file:UseSerializers(LocalDateTimeSerializer::class)

package sql.models

import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperCreatedAt
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.expression.WhereDeclaration
import org.komapper.core.dsl.operator.and
import org.komapper.core.dsl.operator.or
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.core.dsl.query.map
import org.komapper.core.dsl.query.singleOrNull
import serialization.LocalDateTimeSerializer
import sql.Snowflake
import sql.runQuery
import utils.generateId
import utils.generateRandomDiscriminator
import utils.hashPassword
import java.sql.SQLDataException
import java.time.LocalDateTime

@Serializable
@KomapperEntity
data class User(
	@KomapperId @KomapperColumn("user_id") val id: Snowflake,
	val avatarUrl: String? = null,
	@KomapperCreatedAt val createdAt: LocalDateTime = LocalDateTime.now(),
	val description: String? = null,
	val discriminator: Int,
	@Transient val email: String = "",
	@Transient val password: String = "",
	val username: String,
)

object UserController {
	@Throws(SQLDataException::class)
	fun create(username: String, password: String, email: String): User {
		runQuery {
			QueryDsl.from(Meta.user).where {
				Meta.user.email eq email
			}.singleOrNull()
		}?.let {
			throw SQLDataException("User with email '$email' already exists")
		}
		
		val alreadyExistingDiscriminators = runQuery {
			QueryDsl.from(Meta.user).where {
				Meta.user.username eq username
			}.map {
				it.map(User::discriminator)
			}
		}
		
		val discriminator = generateRandomDiscriminator(alreadyExistingDiscriminators)
		val hashedPassword = hashPassword(password)
		
		val user = User(
			id = generateId(),
			discriminator = discriminator.toInt(),
			email = email,
			password = hashedPassword,
			username = username,
		)
		
		return runQuery {
			QueryDsl.insert(Meta.user).single(user)
		}
	}
	
	fun has(id: Snowflake) = get(id) != null
	
	fun get(id: Snowflake) = runQuery {
		QueryDsl.from(Meta.user).where {
			Meta.user.id eq id
		}.singleOrNull()
	}
	
	fun get(username: String, password: String = "", email: String = "") = runQuery {
		val whereEmail: WhereDeclaration = {
			Meta.user.email eq email
		}
		
		val whereUsername: WhereDeclaration = {
			Meta.user.username eq username
		}
		
		val whereUser = (whereEmail.or(whereUsername)).and {
			Meta.user.password eq password
		}
		
		QueryDsl.from(Meta.user).where(whereUser).firstOrNull()
	}
	
}