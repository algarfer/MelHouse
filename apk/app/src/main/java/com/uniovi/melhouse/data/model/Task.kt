package com.uniovi.melhouse.data.model

import android.content.ContentValues
import java.util.UUID

data class Task(
    val id: UUID
)

fun Task.toContentValues(): ContentValues {
    return ContentValues().apply {
        put("id", id.toString())
    }
}
