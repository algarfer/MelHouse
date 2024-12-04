package com.uniovi.melhouse.data.repository.flat

import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.repository.Repository

interface FlatRepository : Repository<Flat> {
    suspend fun joinFlat(invitationCode: String): Flat
    suspend fun createFlat(flat: Flat): Flat
}