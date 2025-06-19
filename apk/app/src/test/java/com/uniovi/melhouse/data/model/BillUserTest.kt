package com.uniovi.melhouse.data.model

import org.junit.Test
import org.junit.Assert.*
import java.util.UUID

class BillUserTest {

    @Test
    fun testBillUserCreation() {
        val billId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val amount = 50.25

        val billUser = BillUser(billId, userId, amount)

        assertEquals(billId, billUser.billId)
        assertEquals(userId, billUser.userId)
        assertEquals(amount, billUser.amount, 0.001)
        assertFalse(billUser.paid)
    }

    @Test
    fun testBillUserDataModification() {
        val billUser = BillUser(
            billId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            amount = 25.0
        )

        billUser.amount = 30.0
        val newBillId = UUID.randomUUID()
        billUser.billId = newBillId
        val newUserId = UUID.randomUUID()
        billUser.userId = newUserId

        assertEquals(30.0, billUser.amount, 0.001)
        assertEquals(newBillId, billUser.billId)
        assertEquals(newUserId, billUser.userId)
    }
}