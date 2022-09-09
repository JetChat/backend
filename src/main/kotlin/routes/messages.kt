package routes

import api.Message
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.flatZip
import org.komapper.core.dsl.query.map
import org.komapper.core.dsl.query.singleOrNull
import sql.models.message
import sql.models.user
import sql.runQuery
import utils.getChannelIdParam
import utils.getIntParam
import utils.getMessageIdParam
import utils.invalidId
import utils.notFound

fun Route.messages() {
	route("/messages") {
		get {
			val after = getIntParam("after")
			val before = getIntParam("before")
			val limit = getIntParam("limit") ?: -1
			
			if (limit == 0) {
				call.respond(emptyList<Message>())
				return@get
			}
			
			if (after == null && before == null) {
				call.respond(emptyList<Message>())
				return@get
			}
			
			if (limit < 0) {
				call.respondText("Invalid limit, must be between 1 and 100")
				return@get
			}
			
			if (after != null && before != null) {
				call.respondText("Cannot specify both 'after' and 'before'")
				return@get
			}
			
			if ((after != null && after < 0) || (before != null && before < 0)) {
				call.respondText("Invalid 'after' or 'before' value, must be greater than 0")
				return@get
			}
			
			val messages = runQuery {
				QueryDsl.from(Meta.message).where {
					Meta.message.channelId eq getChannelIdParam()
				}.where {
					when {
						after != null -> Meta.message.id greater after
						else -> Meta.message.id less before
					}
				}.orderBy(Meta.message.id.desc()).limit(limit)
			}
			
			call.respond(messages)
		}
		
		get("{messageId}") {
			val messageId = getMessageIdParam()
			if (messageId == -1) {
				invalidId("message", messageId)
				return@get
			}
			
			val getMessageAndAuthor = runQuery {
				QueryDsl.from(Meta.message).where {
					Meta.message.id eq messageId
				}.singleOrNull().flatZip {
					QueryDsl.from(Meta.user).where {
						Meta.user.id eq it?.authorId
					}.singleOrNull()
				}.map { (message, user) ->
					if (message == null || user == null) null
					else Message.fromSQL(message, user)
				}
			}
			
			if (getMessageAndAuthor == null) {
				notFound("Message with id '$messageId' not found")
				return@get
			}
			
			call.respond(getMessageAndAuthor)
		}
	}
}