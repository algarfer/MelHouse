package com.uniovi.melhouse.data.model

import org.junit.Test
import org.junit.Assert.*
import java.util.UUID

class UserTest {

    @Test
    fun testUserCreation() {
        val id = UUID.randomUUID()
        val name = "María López"
        val email = "maria@example.com"
        val flatId = UUID.randomUUID()
        val fcmToken = "token123"

        val user = User(id, name, email, fcmToken, flatId)

        assertEquals(id, user.id)
        assertEquals(name, user.name)
        assertEquals(email, user.email)
        assertEquals(flatId, user.flatId)
        assertEquals(fcmToken, user.fcmToken)
        assertTrue(user.tasks.isEmpty())
    }

    @Test
    fun testUserWithNullFlatId() {
        val id = UUID.randomUUID()
        val name = "Carlos Ruiz"
        val email = "carlos@example.com"
        val fcmToken = "tokenCarlos"

        val user = User(id, name, email, fcmToken, null)

        assertEquals(id, user.id)
        assertEquals(name, user.name)
        assertEquals(email, user.email)
        assertNull(user.flatId)
        assertEquals(fcmToken, user.fcmToken)
    }
}