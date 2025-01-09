package com.uniovi.melhouse.data.model

import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

@Serializable
data class Flat(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    val name: String,
    val address: String,
    val floor: Int? = null,
    val door: String? = null,
    val stair: String? = null,
    @SerialName("invitation_code") val invitationCode: String = "",
    @Serializable(with = UUIDSerializer::class) @SerialName("admin_id") val adminId: UUID
)

fun Flat.getFullAddress(): String {
    val details = listOfNotNull(floor?.toString(), stair, door).joinToString(" ")
    return if (details.isNotEmpty()) "$address, $details" else address
}

fun Flat.toJson() = Json.encodeToString(this)

fun String.toFlat() = Json.decodeFromString<Flat>(this)
