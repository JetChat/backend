@file:UseSerializers(LocalDateTimeSerializer::class)

package sql.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperCreatedAt
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import serialization.LocalDateTimeSerializer
import java.time.LocalDateTime

/*
CREATE TABLE jetchat.`user` (
	user_id              INT UNSIGNED NOT NULL  AUTO_INCREMENT  PRIMARY KEY,
	avatar_url           VARCHAR(255)      ,
	created_at           TIMESTAMP  NOT NULL DEFAULT (now())   ,
	description          VARCHAR(511)      ,
	discriminator        DECIMAL(4,0) UNSIGNED NOT NULL    ,
	email                VARCHAR(255)  NOT NULL    ,
	password             VARCHAR(255)  NOT NULL    ,
	username             VARCHAR(40)  NOT NULL    ,
	CONSTRAINT username UNIQUE ( username )
 ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

 */

@Serializable
@KomapperEntity
data class User(
	@KomapperId @KomapperColumn("user_id") val id: Int,
	val avatarUrl: String?,
	@KomapperCreatedAt val createdAt: LocalDateTime,
	val description: String?,
	val discriminator: Int,
	@Transient val email: String = "",
	@Transient val password: String = "",
	val username: String,
)
