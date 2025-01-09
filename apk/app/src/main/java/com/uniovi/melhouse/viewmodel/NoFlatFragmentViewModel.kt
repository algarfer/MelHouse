package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoFlatFragmentViewModel @Inject constructor(
    private val flatRepository: FlatRepository,
    private val prefs: Prefs,
    @ApplicationContext private val applicationContext: Context
) : AbstractViewModel() {

    val flatCodeError: LiveData<String?>
        get() = _flatCodeError
    private val _flatCodeError: MutableLiveData<String?> = MutableLiveData(null)
    val joinFlatSuccess: LiveData<Boolean>
        get() = _joinFlatSuccess
    private val _joinFlatSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    fun joinFlat(flatCode: String) {
        if (flatCode.isBlank()) {
            _flatCodeError.postValue(applicationContext.getString(R.string.error_form_flat_code_empty))
            return
        }

        _flatCodeError.postValue(null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    val flat = flatRepository.joinFlat(flatCode.uppercase())
                    _joinFlatSuccess.postValue(true)
                    prefs.setFlatId(flat.id)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.getMessage(applicationContext))
            }
        }
    }

    override fun clearAllErrors() {
        super.clearAllErrors()
        _flatCodeError.value = null
    }
}