package com.uniovi.melhouse.data.serializers

import com.uniovi.melhouse.data.model.TaskPriority
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TaskPrioritySerializer : KSerializer<TaskPriority> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TaskPriority", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): TaskPriority {
        val value = decoder.decodeInt()
        return TaskPriority.entries.first { it.value == value }
    }

    override fun serialize(encoder: Encoder, value: TaskPriority) {
        encoder.encodeInt(value.value)
    }
}