package com.uniovi.melhouse.data.model

import android.content.ContentValues
import java.util.UUID

data class Flat(
    val id: UUID,
    var name: String,
    var address: String,
    var floor: Int?,
    var door: String?,
    var stair: String?,
    var invitationCode: String,
    var adminId: UUID
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
