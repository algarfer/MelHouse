package com.uniovi.melhouse.data.model

import android.content.ContentValues
import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User (
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    var name: String,
    var email: String,
    @Serializable(with = UUIDSerializer::class) @SerialName("flat_id") var flatId: UUID?
)
