package com.uniovi.melhouse.states

import com.uniovi.melhouse.data.model.Bill
import com.uniovi.melhouse.data.model.BillUser
import com.uniovi.melhouse.data.model.User

data class BillState(val bill: Bill, val mapUserToBillUser: List<Pair<User, BillUser>>) {

}