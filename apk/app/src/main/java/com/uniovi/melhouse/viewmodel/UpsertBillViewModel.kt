package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.model.Bill
import com.uniovi.melhouse.data.model.BillUser
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.bill.BillRepository
import com.uniovi.melhouse.data.repository.billuser.BillUserRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.factories.viewmodel.UpsertBillViewModelFactory
import com.uniovi.melhouse.preference.Prefs
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = UpsertBillViewModelFactory::class
)
class UpsertBillViewModel @AssistedInject constructor(
    private val billRepository: BillRepository,
    private val billUserRepository: BillUserRepository,
    private val userRepository: UserRepository,
    private val prefs: Prefs,
    @Assisted private val bill: Bill?,
    @ApplicationContext private val applicationContext: Context
) : AbstractViewModel() {
    private val MAX_AMOUNT = 1000000

    private val _billUsers = MutableLiveData<List<BillUser>>(emptyList())
    var concept: String

    private val _partners = MutableLiveData<List<User>>(emptyList())
    private val _amount = MutableLiveData<Double?>()
    private val _shares = MutableLiveData<List<Pair<String, Double>>>()
    private val _creationSuccessful = MutableLiveData(false)


    val partners: LiveData<List<User>>
        get() = _partners
    val amount: LiveData<Double?>
        get() = _amount
    val shares: LiveData<List<Pair<String, Double>>>
        get() = _shares
    val creationSuccessful: LiveData<Boolean>
        get() = _creationSuccessful
    val billusers: LiveData<List<BillUser>>
        get() = _billUsers


    fun isBillValid(): Boolean {
        val amount = _amount.value
        val shares = _shares.value
        return when {
            amount == null || shares.isNullOrEmpty() -> {
                _genericError.postValue(applicationContext.getString(R.string.unexpected_error))
                false
            }

            concept.isEmpty() -> {
                _genericError.postValue(applicationContext.getString(R.string.error_empty_concept))
                false
            }

            amount <= 0 -> {
                _genericError.postValue(applicationContext.getString(R.string.error_invalid_amount))
                false
            }

            amount > MAX_AMOUNT -> {
                _genericError.postValue(
                    applicationContext.getString(
                        R.string.error_amount_exceeded,
                        MAX_AMOUNT.toString()
                    )
                )
                false
            }

            shares.sumOf { share -> share.second } == amount -> true
            else -> {
                _genericError.postValue(
                    applicationContext.getString(
                        R.string.error_bill_shares_not_equal,
                        amount.toString()
                    )
                )
                false
            }
        }
    }

    private fun upsert(action: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    action()
                    _creationSuccessful.postValue(true)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.getMessage(applicationContext))
            }
        }
    }

    fun upsertBill() {
        if (bill == null) {
            insertBill()
        } else {
            if (billHasPaidBillUsers(bill))
                _genericError.postValue(
                    applicationContext.getString(R.string.error_bill_has_paid_users)
                )
            else
                updateBill()
        }
    }

    fun billHasPaidBillUsers(bill: Bill): Boolean {
        return _billUsers.value?.any { it.billId == bill.id && it.paid } ?: false
    }

    fun setAmount(amount: String) {
        val arr = amount.split(".")
        if (arr.size > 2) {
            _genericError.postValue(applicationContext.getString(R.string.error_invalid_amount))
            return
        }
        val parsedAmount = try {
            if (arr.size == 1 || arr[1].isEmpty()) {
                arr[0].toDouble()
            } else {
                if (arr[1].length == 1)
                    "${arr[0]}.${arr[1]}".toDouble()
                else
                    "${arr[0]}.${arr[1].substring(0, 2)}".toDouble()
            }
        } catch (e: NumberFormatException) {
            _genericError.postValue(applicationContext.getString(R.string.error_invalid_amount))
            return
        }
        _amount.postValue(parsedAmount)
    }

    private fun updateBill() = upsert {
        billRepository.update(
            bill!!.copy(
                amount = _amount.value!!,
                concept = concept
            )
        )
        _shares.value?.forEach { share ->
            val user = _partners.value!!.find { it.name == share.first }!!
            val billUser = _billUsers.value!!.find { it.userId == user.id }?.copy(
                amount = share.second
            )

            if (billUser == null) {
                if (share.second > 0)
                    billUserRepository.insert(
                        BillUser(
                            userId = user.id,
                            billId = bill.id,
                            amount = share.second
                        )
                    )
                return@forEach
            }

            if (billUser.amount <= 0.0)
                billUserRepository.delete(billUser)
            else
                billUserRepository.update(billUser)
        }
    }

    private fun insertBill() {
        val newBill = Bill(
            amount = _amount.value!!,
            flatId = prefs.getFlatId()!!,
            concept = concept
        )
        upsert {
            billRepository.insert(newBill)
            _partners.value?.forEach { p ->
                val amount = _shares.value!!.find { it.first == p.name }?.second ?: 0.0

                if (amount <= 0)
                    return@forEach

                val bu = BillUser(
                    userId = p.id,
                    billId = newBill.id,
                    amount = amount
                )
                billUserRepository.insert(bu)
            }
        }
    }

    private fun calculatePercentage(share: Double): Double {
        return (share / _amount.value!!) * 100
    }

    fun changeShare(user: String, value: Double) {
        val currentShares = _shares.value ?: emptyList()
        val updatedShares = currentShares.map {
            if (it.first == user) Pair(it.first, value) else it
        }
        _shares.value = updatedShares
    }

    fun updateShares() {
        if (_partners.value.isNullOrEmpty()) {
            return
        }
        if (bill == null) {//Si estamos creando una factura, asignar valores por defecto
            _amount.value = 0.0
            _shares.value = _partners.value!!.map { p -> Pair(p.name, 0.0) }
        } else {
            if (_billUsers.value.isNullOrEmpty())
                return
            _amount.value = bill.amount
            _shares.value = _partners.value!!.map { partner ->
                Pair(
                    partner.name,
                    _billUsers.value?.find { it.userId == partner.id }?.amount ?: 0.0
                )
            }
            concept = bill.concept
        }
    }

    fun getPieChartData(context: Context): PieData {
        val entries = mutableListOf<PieEntry>()

        _shares.value?.forEach { user ->
            val percentage = calculatePercentage(user.second)
            if (percentage > 0) {
                entries.add(PieEntry(percentage.toFloat(), user.first))
            }
        }

        if (entries.sumOf { it.value.toDouble() } < 100) {
            entries.add(
                PieEntry(
                    (100 - entries.sumOf { it.value.toDouble() }).toFloat(),
                    context.getString(R.string.chart_not_available_short)
                )
            )
        }

        val set = PieDataSet(entries, "")
        set.setColors(*context.resources.getIntArray(R.array.chart_colors))
        return PieData(set)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getRoommates(prefs.getFlatId()!!).let {//Cargar compañeros de piso
                _partners.postValue(it)
            }
        }
        bill?.let {
            viewModelScope.launch(Dispatchers.IO) {
                billUserRepository.getBillUsers(it.id)?.let {
                    _billUsers.postValue(it)
                }
            }
        }
        concept = bill?.concept ?: ""
    }


}