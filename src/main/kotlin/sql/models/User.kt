@file:UseSerializers(LocalDateTimeSerializer::class)

package sql.models

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
import org.komapper.core.dsl.query.singleOrNull
import serialization.LocalDateTimeSerializer
import sql.runQuery
import java.time.LocalDateTime

@Serializable
@KomapperEntity
data class User(
	@KomapperId @KomapperColumn("user_id") val id: Int,
	val avatarUrl: String?,
	@KomapperCreatedAt val createdAt: LocalDateTime,
	val description: String?,
	val discriminator: Int,
	@Transient val email: String = "",
	@Transient val password: String = "",
	val username: String,
)

object UserController {
	fun getUser(id: Int) = runQuery {
		QueryDsl.from(Meta.user).where {
			Meta.user.id eq id
		}.singleOrNull()
	}
	
	fun getUser(username: String, password: String = "", email: String = "") = runQuery {
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