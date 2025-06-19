package com.uniovi.melhouse.data.model

import org.junit.Test
import org.junit.Assert.*
import java.util.UUID

class TaskUserTest {

    @Test
    fun testTaskUserCreation() {
        val taskId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val taskUser = TaskUser(
            taskId = taskId,
            userId = userId
        )

        assertEquals(taskId, taskUser.taskId)
        assertEquals(userId, taskUser.userId)
    }

    @Test
    fun testTaskUserEquals() {
        val taskId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val taskUser1 = TaskUser(taskId = taskId, userId = userId)
        val taskUser2 = TaskUser(taskId = taskId, userId = userId)
        val taskUser3 = TaskUser(taskId = UUID.randomUUID(), userId = userId)

        assertEquals(taskUser1, taskUser2)
        assertNotEquals(taskUser1, taskUser3)
    }

    @Test
    fun testTaskUserHashCode() {
        val taskId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val taskUser1 = TaskUser(taskId = taskId, userId = userId)
        val taskUser2 = TaskUser(taskId = taskId, userId = userId)

        assertEquals(taskUser1.hashCode(), taskUser2.hashCode())
    }

    @Test
    fun testToString() {
        val taskId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val taskUser = TaskUser(taskId = taskId, userId = userId)

        val toString = taskUser.toString()
        assertTrue(toString.contains(taskId.toString()))
        assertTrue(toString.contains(userId.toString()))
    }
}