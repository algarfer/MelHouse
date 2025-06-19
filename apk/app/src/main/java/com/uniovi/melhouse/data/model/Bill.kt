package com.uniovi.melhouse.data.model

import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

@Serializable
data class Bill(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class) @SerialName("flat_id") var flatId: UUID,
    var amount: Double,
    var concept: String,
)

fun Bill.toJson(): String {
    return Json.encodeToString(this)
}

fun String.toBill(): Bill {
    val json = Json {
        ignoreUnknownKeys = false
        encodeDefaults = true
    }

    return json.decodeFromString<Bill>(this)
}