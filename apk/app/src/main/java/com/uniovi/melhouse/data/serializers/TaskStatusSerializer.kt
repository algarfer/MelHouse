package com.uniovi.melhouse.data.serializers

import com.uniovi.melhouse.data.model.TaskStatus
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TaskStatusSerializer : KSerializer<TaskStatus> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TaskStatus", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): TaskStatus {
        val value = decoder.decodeInt()
        return TaskStatus.entries.first { it.value == value }
    }

    override fun serialize(encoder: Encoder, value: TaskStatus) {
        encoder.encodeInt(value.value)
    }
}