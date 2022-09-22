package routes

import api.CreateMessagePayload
import api.GetMessagePayload
import api.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.singleOrNull
import sql.isValid
import sql.models.MessageController
import sql.models.message
import sql.runQuery
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
			
			if (after?.isValid() == false) {
				call.respondText("Invalid 'after' id")
				return@get
			}
			
			if (before?.isValid() == false) {
				call.respondText("Invalid 'before' id")
				return@get
			}
			
			val messages = MessageController.get(getChannelIdParam(), limit, before, after)
			call.respond(messages)
		}
		
		route("{messageId}") {
			get {
				val messageId = getMessageIdParam()
				if (messageId == -1L) {
					invalidId("message", messageId)
					return@get
				}
				
				val getMessageAndAuthor = MessageController.getWithAuthor(messageId)
				
				if (getMessageAndAuthor == null) {
					notFound("message", messageId)
					return@get
				}
				
				call.respond(getMessageAndAuthor)
			}
			
			delete {
				val messageId = getMessageIdParam()
				if (messageId == -1L) {
					invalidId("message", messageId)
					return@delete
				}
				
				val message = runQuery {
					QueryDsl.from(Meta.message).where {
						Meta.message.id eq messageId
					}.singleOrNull()
				}
				
				if (message == null) {
					notFound("message", messageId)
					return@delete
				}
				
				val session = call.principal<UserSession>()!!
				if (message.authorId != session.userId) {
					call.respondText("Cannot delete message that is not yours")
					return@delete
				}
				
				MessageController.delete(messageId)
				call.respond(message)
			}
		}
		
		post("create") {
			val body = call.receive<CreateMessagePayload>()
			val session = call.principal<UserSession>()!!
			
			if (body.content == null) {
				call.respondText("Missing 'content' parameter")
				return@post
			}
			
			val lastMessage = MessageController.create(
				channelId = getChannelIdParam(),
				authorId = session.userId,
				content = body.content,
				replyId = body.replyTo
			)
			
			call.respond(lastMessage)
		}
	}
}