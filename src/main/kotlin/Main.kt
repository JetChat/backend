
import api.UserSession
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.util.logging.*
import kotlinx.serialization.json.Json
import org.komapper.dialect.mysql.jdbc.MySqlJdbcDialect
import org.komapper.jdbc.JdbcDatabase
import routes.auth
import routes.guilds
import routes.users
import java.time.Duration

fun env(key: String, default: String) = System.getenv(key) ?: default
fun env(key: String) = System.getenv(key) ?: null

fun main(args: Array<String>) = EngineMain.main(args)

lateinit var db: JdbcDatabase

val logger = KtorSimpleLogger("main")

fun Application.module() {
	db = JdbcDatabase(
		url = "jdbc:mysql://${env("MYSQL_URL")}/${env(("DATABASE"))}",
		dialect = MySqlJdbcDialect(),
		user = env("USER")!!,
		password = env("PASSWORD")!!
	)
	
	val sessionCookieKey = env("SESSION_COOKIE")!!.toByteArray()
	
	install(Authentication) {
		session<UserSession>("auth-session") {
			validate { session ->
				when {
					session.userId != 0L -> session
					else -> null
				}
			}
			
			challenge {
				call.respond(UnauthorizedResponse())
			}
		}
	}
	install(DefaultHeaders)
	install(ContentNegotiation) {
		json(Json {
			prettyPrint = true
		})
	}
	install(Resources)
	install(Sessions) {
		cookie<UserSession>("user_session") {
			cookie.path = "/"
			cookie.maxAgeInSeconds = Duration.ofMinutes(5).seconds
			
			transform(SessionTransportTransformerMessageAuthentication(sessionCookieKey))
		}
	}
	install(WebSockets) {
		pingPeriod = Duration.ofSeconds(30)
	}
	
	configureRouting()
}

fun Application.configureRouting() {
	routing {
		auth()
		
		authenticate("auth-session") {
			guilds()
			users()
		}
	}
}
