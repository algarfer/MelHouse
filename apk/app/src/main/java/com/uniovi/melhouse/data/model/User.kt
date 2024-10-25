package com.uniovi.melhouse.data.model

import android.content.ContentValues
import java.util.UUID

data class User (
    val id: UUID,
    var name: String,
    var email: String,
    var flatId: UUID?
)

fun User.toContentValues(): ContentValues {
    return ContentValues().apply {
        put("id", id.toString())
        put("name", name)
        put("email", email)
        put("flat_id", flatId?.toString())
    }
}
