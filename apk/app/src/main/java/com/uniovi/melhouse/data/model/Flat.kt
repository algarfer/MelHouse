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
    var name: String,
    var address: String,
    var floor: Int?,
    var door: String?,
    var stair: String?,
    @SerialName("invitation_code") var invitationCode: String = "",
    @Serializable(with = UUIDSerializer::class) @SerialName("admin_id") var adminId: UUID
)

fun Flat.getFullAddress(): String {
    val details = listOfNotNull(floor?.toString(), stair, door).joinToString("")
    return if (details.isNotEmpty()) "$address, $details" else address
}

fun Flat.toJson(): String {
    return Json.encodeToString(this)
}

fun String.toFlat(): Flat {
    return Json.decodeFromString(this)
}
