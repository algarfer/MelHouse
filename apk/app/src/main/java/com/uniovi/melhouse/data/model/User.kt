package com.uniovi.melhouse.data.model

import android.content.ContentValues
import java.util.UUID

data class User(
    val id: UUID
)

fun User.toContentValues(): ContentValues {
    return ContentValues().apply {
        put("id", id.toString())
    }
}
