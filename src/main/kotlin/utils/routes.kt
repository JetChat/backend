package utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

fun PipelineContext<*, ApplicationCall>.getIntParam(name: String) = call.parameters[name]?.toIntOrNull()

fun PipelineContext<*, ApplicationCall>.getIdParam() = getIntParam("id") ?: -1

fun PipelineContext<*, ApplicationCall>.getChannelIdParam() = getIntParam("channelId") ?: -1

fun PipelineContext<*, ApplicationCall>.getGuildIdParam() = getIntParam("guildId") ?: -1

fun PipelineContext<*, ApplicationCall>.getMemberIdParam() = getIntParam("memberId") ?: -1

fun PipelineContext<*, ApplicationCall>.getMessageIdParam() = getIntParam("messageId") ?: -1

fun PipelineContext<*, ApplicationCall>.getUserIdParam() = getIntParam("userId") ?: -1


suspend fun PipelineContext<*, ApplicationCall>.notFound(message: String = "Not found") = call.respondText(message, status = HttpStatusCode.NotFound)
suspend fun PipelineContext<*, ApplicationCall>.invalidId(name: String, id: Int) = call.respondText("Invalid $name id '$id'", status = HttpStatusCode.BadRequest)