package utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import java.util.*

fun PipelineContext<*, ApplicationCall>.getStringQueryParam(name: String) = call.request.queryParameters[name]
fun PipelineContext<*, ApplicationCall>.getIntQueryParam(name: String) = call.request.queryParameters[name]?.toLongOrNull()

fun PipelineContext<*, ApplicationCall>.getLongPathParam(name: String) = call.parameters[name]?.toLongOrNull()

fun PipelineContext<*, ApplicationCall>.getIdParam() = getLongPathParam("id") ?: -1

fun PipelineContext<*, ApplicationCall>.getChannelIdParam() = getLongPathParam("channelId") ?: -1

fun PipelineContext<*, ApplicationCall>.getGuildIdParam() = getLongPathParam("guildId") ?: -1

fun PipelineContext<*, ApplicationCall>.getMemberIdParam() = getLongPathParam("memberId") ?: -1

fun PipelineContext<*, ApplicationCall>.getMessageIdParam() = getLongPathParam("messageId") ?: -1

fun PipelineContext<*, ApplicationCall>.getUserIdParam() = getLongPathParam("userId") ?: -1

internal fun String.capitalizeFirstLetter() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

suspend fun PipelineContext<*, ApplicationCall>.notFound(name: String, id: Long? = null) = when (id) {
	null -> call.respondText("${name.capitalizeFirstLetter()} not found", status = HttpStatusCode.NotFound)
	else -> call.respondText("${name.capitalizeFirstLetter()} with id '$id' not found", status = HttpStatusCode.NotFound)
}
suspend fun PipelineContext<*, ApplicationCall>.invalidId(name: String, id: Long) = call.respondText("Invalid $name id '$id'", status = HttpStatusCode.BadRequest)

suspend fun PipelineContext<*, ApplicationCall>.badRequest(message: String = "Bad request") = call.respondText(message, status = HttpStatusCode.BadRequest)
suspend fun PipelineContext<*, ApplicationCall>.verifyStringLength(value: String, range: IntRange, name: String) = if (value.length !in range) badRequest("${name.capitalizeFirstLetter()} must be between ${range.first} and ${range.last} characters long") else Unit