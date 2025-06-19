package com.uniovi.melhouse.data.model

import com.uniovi.melhouse.utils.getDatesString
import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDate
import java.util.UUID

class TaskTest {

    @Test
    fun testTaskCreation() {
        val id = UUID.randomUUID()
        val name = "Limpiar cocina"
        val description = "Limpiar la cocina completamente"
        val startDate = LocalDate.now()
        val endDate = LocalDate.now().plusDays(1)
        val flatId = UUID.randomUUID()
        val priority = TaskPriority.HIGH
        val status = TaskStatus.PENDING
        val assignees = setOf<User>()

        val task = Task(
            id = id,
            name = name,
            description = description,
            startDate = startDate,
            endDate = endDate,
            flatId = flatId,
            priority = priority,
            status = status,
            assignees = assignees
        )

        assertEquals(id, task.id)
        assertEquals(name, task.name)
        assertEquals(description, task.description)
        assertEquals(startDate, task.startDate)
        assertEquals(endDate, task.endDate)
        assertEquals(flatId, task.flatId)
        assertEquals(priority, task.priority)
        assertEquals(status, task.status)
        assertEquals(assignees, task.assignees)
        assertTrue(task.assignees.isEmpty())
    }

    @Test
    fun testTaskWithNullValues() {
        val id = UUID.randomUUID()
        val name = "Sacar la basura"
        val flatId = UUID.randomUUID()

        val task = Task(
            id = id,
            name = name,
            description = null,
            startDate = null,
            endDate = null,
            flatId = flatId,
            priority = null,
            status = null,
            assignees = setOf()
        )

        assertEquals(id, task.id)
        assertEquals(name, task.name)
        assertNull(task.description)
        assertNull(task.startDate)
        assertNull(task.endDate)
        assertEquals(flatId, task.flatId)
        assertNull(task.priority)
        assertNull(task.status)
        assertTrue(task.assignees.isEmpty())
    }

    @Test
    fun testAddAssignee() {
        val task = Task(
            id = UUID.randomUUID(),
            name = "Comprar comida",
            flatId = UUID.randomUUID(),
            assignees = mutableSetOf()
        )

        val user = User(
            id = UUID.randomUUID(),
            name = "Juan García",
            email = "juan@example.com",
            fcmToken = "token123",
            flatId = task.flatId
        )

        val updatedTask = task.copy(assignees = task.assignees + user)

        assertEquals(1, updatedTask.assignees.size)
        assertTrue(updatedTask.assignees.contains(user))
    }

    @Test
    fun testGetDatesString() {
        val task = Task(
            id = UUID.randomUUID(),
            name = "Revisar contrato",
            flatId = UUID.randomUUID(),
            startDate = LocalDate.of(2023, 5, 10),
            endDate = LocalDate.of(2023, 5, 15)
        )

        val datesString = task.getDatesString()
        assertNotNull(datesString)
    }

    @Test
    fun testTaskStatusChange() {
        val task = Task(
            id = UUID.randomUUID(),
            name = "Pagar facturas",
            flatId = UUID.randomUUID(),
            status = TaskStatus.PENDING
        )

        val completedTask = task.copy(status = TaskStatus.DONE)

        assertEquals(TaskStatus.DONE, completedTask.status)
        assertNotEquals(task.status, completedTask.status)
    }

    @Test
    fun testTaskSerializationWithoutTransientFields() {
        val id = UUID.randomUUID()
        val name = "Limpiar baño"
        val flatId = UUID.randomUUID()
        val task = Task(
            id = id,
            name = name,
            flatId = flatId,
            priority = TaskPriority.MEDIUM
        )

        val json = task.toJson(withTransientFields = false)

        assertTrue(json.contains("\"name\":\"Limpiar baño\""))
        assertTrue(json.contains("\"id\":\"$id\""))
        assertTrue(json.contains("\"flat_id\":\"$flatId\""))
        assertTrue(json.contains("\"priority\":1"))
        assertFalse(json.contains("\"assignees\""))
    }

    @Test
    fun testTaskSerializationWithTransientFields() {
        val id = UUID.randomUUID()
        val name = "Lavar platos"
        val flatId = UUID.randomUUID()
        val user = User(
            id = UUID.randomUUID(),
            name = "Ana López",
            email = "ana@example.com",
            flatId = flatId,
            fcmToken = null
        )

        val task = Task(
            id = id,
            name = name,
            flatId = flatId,
            assignees = setOf(user)
        )

        val json = task.toJson(withTransientFields = true)

        assertTrue(json.contains("\"name\":\"Lavar platos\""))
        assertTrue(json.contains("\"assignees\""))
        assertTrue(json.contains(user.id.toString()))
    }

    @Test
    fun testTaskDeserialization() {
        val id = UUID.randomUUID()
        val flatId = UUID.randomUUID()
        val json = """
        {
            "id": "$id",
            "name": "Comprar detergente",
            "priority": 2,
            "flat_id": "$flatId"
        }
    """.trimIndent()

        val task = json.toTask()

        assertEquals(id, task.id)
        assertEquals("Comprar detergente", task.name)
        assertEquals(TaskPriority.HIGH, task.priority)
        assertEquals(flatId, task.flatId)
        assertTrue(task.assignees.isEmpty())
    }

    @Test
    fun testTaskRoundTripSerialization() {
        val originalTask = Task(
            name = "Organizar reunión",
            description = "Reunión semanal de piso",
            status = TaskStatus.PENDING,
            priority = TaskPriority.LOW,
            startDate = LocalDate.now(),
            flatId = UUID.randomUUID()
        )

        val json = originalTask.toJson()
        val deserializedTask = json.toTask()

        assertEquals(originalTask.id, deserializedTask.id)
        assertEquals(originalTask.name, deserializedTask.name)
        assertEquals(originalTask.description, deserializedTask.description)
        assertEquals(originalTask.status, deserializedTask.status)
        assertEquals(originalTask.priority, deserializedTask.priority)
        assertEquals(originalTask.startDate, deserializedTask.startDate)
        assertEquals(originalTask.flatId, deserializedTask.flatId)
    }
}