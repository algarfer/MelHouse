package com.uniovi.melhouse.data.model

import android.content.ContentValues
import java.util.UUID

data class Flat(
    val id: UUID
)

fun Flat.toContentValues(): ContentValues {
    return ContentValues().apply {
        put("id", id.toString())
    }
}
