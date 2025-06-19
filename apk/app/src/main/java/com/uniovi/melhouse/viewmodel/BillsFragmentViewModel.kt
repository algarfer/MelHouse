package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.Bill
import com.uniovi.melhouse.data.repository.bill.BillRepository
import com.uniovi.melhouse.data.repository.billuser.BillUserRepository
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.states.BillState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillsFragmentViewModel @Inject constructor(
    private var billRepository: BillRepository,
    private var billUserRepository: BillUserRepository,
    userRepository: UserRepository,
    private var userSessionFacade: SupabaseUserSessionFacade,
    flatRepository: FlatRepository,
    prefs: Prefs
) : AbstractViewModel() {

    private val _currentUser = userRepository.findByIdAsFlow(userSessionFacade.getUserId()!!)
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)
    private val _flat = flatRepository.findByIdAsFlow(prefs.getFlatId()!!)
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(viewModelScope, started = SharingStarted.Lazily)
    val isAdmin: LiveData<Boolean> = _flat.combine(_currentUser) { flat, user ->
        flat.adminId == user.id
    }.catch { e -> _genericError.postValue(e.localizedMessage) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private val bills = billRepository.findAllByFlatIdAsFlow(prefs.getFlatId()!!)
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(replayExpirationMillis = 5000),
            replay = 1
        )

    private val billUsers = billUserRepository.findAllAsFlow()
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(replayExpirationMillis = 5000),
            replay = 1
        )

    private val roommates = userRepository.getRoommatesAsFlow(prefs.getFlatId()!!)
        .catch { e -> _genericError.postValue(e.localizedMessage) }
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(replayExpirationMillis = 5000),
            replay = 1
        )


    private val billBillUsers = bills.combine(billUsers) { bills, billUsers ->
        bills.map { bill ->
            Pair(bill, billUsers.filter { it.billId == bill.id })
        }
    }.shareIn(viewModelScope, started = SharingStarted.Lazily)

    val billStates = roommates.combine(billBillUsers) { roomates, billBillUsers ->
        billBillUsers.filter { it.second.isNotEmpty() }.map { bbu ->
            val mapUserToBillUser = bbu.second.map { bu ->
                Pair(roomates.first { it.id == bu.userId }, bu)
            }
            BillState(bbu.first, mapUserToBillUser)
        }
    }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    /*val billStates = bills.combine(billUsers) { bills, billUsers ->
        bills.map { bill ->
            val mapNameToAmount = billUsers.filter { it.billId == bill.id }
                .associateBy { userRepository.findById(it.userId)!! }
                .toList()
            BillState(bill, mapNameToAmount)
        }
    }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)*/

    fun deleteBill(bill: Bill) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    billRepository.delete(bill)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.localizedMessage)
            }
        }

    }

    fun payActiveUserBill(item: BillState) {
        val bu =
            item.mapUserToBillUser.first { it.first.id == userSessionFacade.getUserId() }.second

        if (!bu.paid) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Executor.safeCall {
                        billUserRepository.update(bu.copy(paid = true))
                    }
                } catch (e: PersistenceLayerException) {
                    _genericError.postValue(e.localizedMessage)
                }
            }
        } else
            _genericError.postValue("Ya has pagado esta factura")
    }

    fun userHasToPay(item: BillState): Boolean {
        return item.mapUserToBillUser.map { it.second.userId }
            .contains(userSessionFacade.getUserId())
    }
}