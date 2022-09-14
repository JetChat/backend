package routes

import api.CreateMessagePayload
import api.GetMessagePayload
import api.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import logger
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.flatZip
import org.komapper.core.dsl.query.map
import org.komapper.core.dsl.query.singleOrNull
import sql.models.Message
import sql.models.message
import sql.models.user
import sql.runQuery
import utils.generateId
import utils.getChannelIdParam
import utils.getLongPathParam
import utils.getMessageIdParam
import utils.invalidId
import utils.notFound

fun Route.messages() {
	route("/messages") {
		get {
			val after = getLongPathParam("after")
			val before = getLongPathParam("before")
			val limit = getLongPathParam("limit")?.toInt() ?: -1
			
			if (limit == 0) {
				call.respond(emptyList<GetMessagePayload>())
				return@get
			}
			
			if (after == null && before == null) {
				call.respond(emptyList<GetMessagePayload>())
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
			if (messageId == -1L) {
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
					else GetMessagePayload.fromSQL(message, user)
				}
			}
			
			if (getMessageAndAuthor == null) {
				notFound("message", messageId)
				return@get
			}
			
			call.respond(getMessageAndAuthor)
		}
		
		post("create") {
			val body = call.receive<CreateMessagePayload>()
			val session = call.principal<UserSession>()!!
			
			if (body.content == null) {
				call.respondText("Missing 'content' parameter")
				return@post
			}
			
			logger.debug(session.toString())
			
			val lastMessage = runQuery {
				QueryDsl.insert(Meta.message).single(
					Message(
						id = generateId(),
						channelId = getChannelIdParam(),
						authorId = session.userId,
						content = body.content,
						replyId = body.replyTo
					)
				)
			}
			
			call.respond(lastMessage)
		}
	}
}