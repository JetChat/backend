package sql

import db
import org.komapper.core.dsl.query.Query
import org.komapper.core.dsl.query.QueryScope
import org.komapper.tx.core.TransactionOperator

fun <T> runQuery(query: QueryScope.(TransactionOperator) -> Query<T>): T = db.withTransaction {
	db.runQuery {
		query(it)
	}
}

@JvmName("runQuery1")
fun <T> runQueryNullable(query: QueryScope.(TransactionOperator) -> Query<T>?): T = db.withTransaction {
	db.runQuery {
		query(it) ?: throw IllegalStateException()
	}
}
