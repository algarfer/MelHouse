package com.uniovi.melhouse.data.model

import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.UUID

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    @SerialName("fcm_token") val fcmToken: String?,
    @Serializable(with = UUIDSerializer::class) @SerialName("flat_id") val flatId: UUID? = null,
    @Transient var tasks: Set<Task> = emptySet()
)

fun User.getInitials(): String {
    return name.substring(0, 1).uppercase()
}

fun User.toJson(withTransientFields: Boolean = false): String {
    if (!withTransientFields) return Json.encodeToString(this)

    val json = Json.encodeToJsonElement(this).jsonObject
    val tasksJson = Json.encodeToJsonElement(this.tasks.map { it.toJson() })
    val extendedJson = json + ("tasks" to tasksJson)
    return Json.encodeToString(JsonObject(extendedJson))
}

fun String.toUser(withTransientFields: Boolean = false): User {
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    if (!withTransientFields) return json.decodeFromString<User>(this)

    val jsonObject = json.parseToJsonElement(this).jsonObject
    val baseUser = json.decodeFromJsonElement<User>(jsonObject)
    val tasksJsonArray = jsonObject["tasks"]?.jsonArray ?: emptyList()
    val tasks = tasksJsonArray.map { it.jsonPrimitive.content.toTask() }
    return baseUser.copy(tasks = tasks.toSet())
}
