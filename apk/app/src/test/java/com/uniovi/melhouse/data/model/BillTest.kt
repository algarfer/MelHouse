package com.uniovi.melhouse.data.model

import org.junit.Test
import org.junit.Assert.*
import java.util.UUID

class BillTest {

    @Test
    fun testBillCreation() {
        val id = UUID.randomUUID()
        val flatId = UUID.randomUUID()
        val amount = 100.50
        val concept = "Factura de luz"

        val bill = Bill(id, flatId, amount, concept)

        assertEquals(id, bill.id)
        assertEquals(flatId, bill.flatId)
        assertEquals(amount, bill.amount, 0.001)
        assertEquals(concept, bill.concept)
    }

    @Test
    fun testBillDefaultIdGeneration() {
        val flatId = UUID.randomUUID()

        val bill = Bill(flatId = flatId, amount = 50.0, concept = "Agua")

        assertNotNull(bill.id)
    }

    @Test
    fun testBillSerialization() {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val flatId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val bill = Bill(id, flatId, 75.0, "Internet")

        val json = bill.toJson()

        assertTrue(json.contains("\"id\":\"00000000-0000-0000-0000-000000000001\""))
        assertTrue(json.contains("\"flat_id\":\"00000000-0000-0000-0000-000000000002\""))
        assertTrue(json.contains("\"amount\":75.0"))
        assertTrue(json.contains("\"concept\":\"Internet\""))
    }

    @Test
    fun testBillDeserialization() {
        val json = """{"id":"00000000-0000-0000-0000-000000000001","flat_id":"00000000-0000-0000-0000-000000000002","amount":75.0,"concept":"Internet"}"""

        val bill = json.toBill()

        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000001"), bill.id)
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000002"), bill.flatId)
        assertEquals(75.0, bill.amount, 0.001)
        assertEquals("Internet", bill.concept)
    }

    @Test
    fun testBillDataModification() {
        val bill = Bill(
            flatId = UUID.randomUUID(),
            amount = 100.0,
            concept = "Gas"
        )

        bill.amount = 150.0
        bill.concept = "Gas actualizado"
        val newFlatId = UUID.randomUUID()
        bill.flatId = newFlatId

        assertEquals(150.0, bill.amount, 0.001)
        assertEquals("Gas actualizado", bill.concept)
        assertEquals(newFlatId, bill.flatId)
    }

    @Test
    fun testRoundTripSerialization() {
        val originalBill = Bill(
            id = UUID.randomUUID(),
            flatId = UUID.randomUUID(),
            amount = 123.45,
            concept = "Factura de teléfono"
        )

        val json = originalBill.toJson()
        val deserializedBill = json.toBill()

        assertEquals(originalBill.id, deserializedBill.id)
        assertEquals(originalBill.flatId, deserializedBill.flatId)
        assertEquals(originalBill.amount, deserializedBill.amount, 0.001)
        assertEquals(originalBill.concept, deserializedBill.concept)
    }
}