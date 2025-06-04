package com.uniovi.melhouse.data.model

import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BillUser(
    @Serializable(with = UUIDSerializer::class) @SerialName("bill_id") var billId: UUID,
    @Serializable(with = UUIDSerializer::class) @SerialName("user_id") var userId: UUID,
    var amount: Double,
    var paid: Boolean = false
)