package com.uniovi.melhouse.data.repository.bill

import com.uniovi.melhouse.data.model.Bill
import com.uniovi.melhouse.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface BillRepository : Repository<Bill> {
    fun findAllByFlatIdAsFlow(flatId: UUID): Flow<List<Bill>>
}