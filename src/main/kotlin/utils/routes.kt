package utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

fun PipelineContext<*, ApplicationCall>.getStringQueryParam(name: String) = call.request.queryParameters[name]
fun PipelineContext<*, ApplicationCall>.getIntQueryParam(name: String) = call.request.queryParameters[name]?.toLongOrNull()

fun PipelineContext<*, ApplicationCall>.getLongPathParam(name: String) = call.parameters[name]?.toLongOrNull()

fun PipelineContext<*, ApplicationCall>.getIdParam() = getLongPathParam("id") ?: -1

fun PipelineContext<*, ApplicationCall>.getChannelIdParam() = getLongPathParam("channelId") ?: -1

fun PipelineContext<*, ApplicationCall>.getGuildIdParam() = getLongPathParam("guildId") ?: -1

fun PipelineContext<*, ApplicationCall>.getMemberIdParam() = getLongPathParam("memberId") ?: -1

fun PipelineContext<*, ApplicationCall>.getMessageIdParam() = getLongPathParam("messageId") ?: -1

fun PipelineContext<*, ApplicationCall>.getUserIdParam() = getLongPathParam("userId") ?: -1


suspend fun PipelineContext<*, ApplicationCall>.notFound(message: String = "Not found") = call.respondText(message, status = HttpStatusCode.NotFound)
suspend fun PipelineContext<*, ApplicationCall>.invalidId(name: String, id: Long) = call.respondText("Invalid $name id '$id'", status = HttpStatusCode.BadRequest)