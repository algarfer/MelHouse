package com.uniovi.melhouse.data.model

import com.uniovi.melhouse.data.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TaskUser(
    @Serializable(with = UUIDSerializer::class) @SerialName("user_id") var userId: UUID,
    @Serializable(with = UUIDSerializer::class) @SerialName("task_id") var taskId: UUID
)
