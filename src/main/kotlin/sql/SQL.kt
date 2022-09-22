package sql

import db
import kotlinx.datetime.Instant
import org.komapper.core.dsl.query.Query
import org.komapper.core.dsl.query.QueryScope
import org.komapper.tx.core.TransactionOperator

typealias Snowflake = Long

fun Snowflake.toInstant() = toString().dropLast(4).toLong().let { Instant.fromEpochMilliseconds(it) }.also {
	require(it.epochSeconds > 0) { "Invalid snowflake: $this" }
}

fun Snowflake.isValid() = runCatching { toInstant() }.isSuccess

fun <T> runQuery(query: QueryScope.(TransactionOperator) -> Query<T>): T = db.withTransaction {
	db.runQuery {
		query(it)
	}
}
