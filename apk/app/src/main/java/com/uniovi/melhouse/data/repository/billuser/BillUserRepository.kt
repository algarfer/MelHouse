package com.uniovi.melhouse.data.repository.billuser

import com.uniovi.melhouse.data.model.BillUser
import com.uniovi.melhouse.data.repository.Repository
import java.util.UUID

interface BillUserRepository : Repository<BillUser> {
    suspend fun getBillUsers(billId: UUID): List<BillUser>?
}