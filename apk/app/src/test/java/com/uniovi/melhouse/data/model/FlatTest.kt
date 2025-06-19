package com.uniovi.melhouse.data.model

import org.junit.Test
import org.junit.Assert.*
import java.util.UUID

class FlatTest {

    @Test
    fun testFlatCreation() {
        val id = UUID.randomUUID()
        val name = "Piso Centro"
        val address = "Calle Principal 123"
        val adminId = UUID.randomUUID()

        val flat = Flat(
            id = id,
            name = name,
            address = address,
            adminId = adminId
        )

        assertEquals(id, flat.id)
        assertEquals(name, flat.name)
        assertEquals(address, flat.address)
        assertEquals(adminId, flat.adminId)
    }

    @Test
    fun testFlatSerialization() {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000003")
        val flat = Flat(
            id = id,
            name = "Piso Estudiantes",
            address = "Calle Universidad 20",
            adminId = UUID.fromString("00000000-0000-0000-0000-000000000001"),
        )

        val json = flat.toJson()

        assertTrue(json.contains("\"id\":\"00000000-0000-0000-0000-000000000003\""))
        assertTrue(json.contains("\"name\":\"Piso Estudiantes\""))
        assertTrue(json.contains("\"address\":\"Calle Universidad 20\""))
        assertTrue(json.contains("\"admin_id\":\"00000000-0000-0000-0000-000000000001\""))
    }

    @Test
    fun testFlatDeserialization() {
        val json = """
        {
            "id": "00000000-0000-0000-0000-000000000004",
            "name": "Piso Playa",
            "address": "Avenida Marítima 30",
            "admin_id": "00000000-0000-0000-0000-000000000005"
        }
        """.trimIndent()

        val flat = json.toFlat()

        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000004"), flat.id)
        assertEquals("Piso Playa", flat.name)
        assertEquals("Avenida Marítima 30", flat.address)
        assertEquals("00000000-0000-0000-0000-000000000005", flat.adminId.toString())
    }

    @Test
    fun testFlatRoundTripSerialization() {
        val originalFlat = Flat(
            name = "Piso Centro",
            address = "Plaza Mayor 5",
            adminId = UUID.randomUUID()
        )

        val json = originalFlat.toJson()
        val deserializedFlat = json.toFlat()

        assertEquals(originalFlat.id, deserializedFlat.id)
        assertEquals(originalFlat.name, deserializedFlat.name)
        assertEquals(originalFlat.address, deserializedFlat.address)
        assertEquals(originalFlat.adminId, deserializedFlat.adminId)
    }
}