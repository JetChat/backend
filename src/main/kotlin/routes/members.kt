package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.singleOrNull
import sql.models.member
import sql.runQuery
import utils.getMemberIdParam
import utils.invalidId
import utils.notFound

fun Route.members() {
	route("members") {
		route("{memberId}") {
			get {
				val memberId = getMemberIdParam()
				if (memberId == -1L) {
					invalidId("member", memberId)
					return@get
				}
				
				val getMember = runQuery {
					QueryDsl.from(Meta.member).where {
						Meta.member.id eq memberId
					}.singleOrNull()
				} ?: return@get notFound("Member with id '$memberId' not found")
				
				call.respond(getMember)
			}
		}
	}
}