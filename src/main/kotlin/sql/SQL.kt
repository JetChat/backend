package sql

import db
import org.komapper.core.dsl.query.Query
import org.komapper.core.dsl.query.QueryScope
import org.komapper.tx.core.TransactionOperator

typealias Snowflake = Long

fun <T> runQuery(query: QueryScope.(TransactionOperator) -> Query<T>): T = db.withTransaction {
	db.runQuery {
		query(it)
	}
}
