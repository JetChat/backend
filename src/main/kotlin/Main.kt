
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import org.komapper.dialect.mysql.jdbc.MySqlJdbcDialect
import org.komapper.jdbc.JdbcDatabase
import routes.guilds
import routes.users
import java.time.Duration

fun env(key: String, default: String? = null) = System.getenv(key) ?: default

fun main(args: Array<String>) = EngineMain.main(args)

lateinit var db: JdbcDatabase

fun Application.module() {
	db = JdbcDatabase(
		url = "jdbc:mysql://${env("MYSQL_URL")}/${env(("DATABASE"))}",
		dialect = MySqlJdbcDialect(),
		user = env("USER")!!,
		password = env("PASSWORD")!!
	)
	
	install(DefaultHeaders)
	install(ContentNegotiation) {
		json(Json {
			prettyPrint = true
		})
	}
	install(Resources)
	install(WebSockets) {
		pingPeriod = Duration.ofSeconds(30)
	}
	
	configureRouting()
}

fun Application.configureRouting() {
	routing {
		guilds()
		users()
	}
}
