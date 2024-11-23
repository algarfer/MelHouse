package com.uniovi.melhouse.data.model

import android.content.ContentValues
import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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

fun Flat.toContentValues(): ContentValues {
    return ContentValues().apply {
        put("id", id.toString())
        put("name", name)
        put("address", address)
        put("floor", floor)
        put("door", door)
        put("stair", stair)
        put("invitation_code", invitationCode)
        put("admin_id", adminId.toString())
    }
}
