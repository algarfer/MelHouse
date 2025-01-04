package com.uniovi.melhouse.data.model

import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

@Serializable
data class User (
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    @Serializable(with = UUIDSerializer::class) @SerialName("flat_id") val flatId: UUID? = null,
    @Transient var tasks: List<Task> = emptyList()
)

fun User.getInitials(): String {
    return name.substring(0, 1).uppercase()
}

fun User.toJson() = Json.encodeToString(this)

fun String.toUser() = Json.decodeFromString<User>(this)
