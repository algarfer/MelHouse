package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoFlatFragmentViewModel @Inject constructor(
    private val flatRepository: FlatRepository
) : ViewModel() {

    val flatCodeError: LiveData<String?>
        get() = _flatCodeError
    private val _flatCodeError: MutableLiveData<String?> = MutableLiveData(null)

    val joinFlatSuccess: LiveData<Boolean>
        get() = _joinFlatSuccess
    private val _joinFlatSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    val snackBarMsg: LiveData<String?>
        get() = _snackBarMsg
    private val _snackBarMsg: MutableLiveData<String?> = MutableLiveData(null)


    fun joinFlat(flatCode: String, context: Context) {

        if (flatCode.isBlank()) {
            _flatCodeError.postValue(context.getString(R.string.error_form_flat_code_empty))
            return
        }

        _flatCodeError.postValue(null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                flatRepository.joinFlat(flatCode.uppercase())
                _joinFlatSuccess.postValue(true)
            } catch (e: PersistenceLayerException) {
                _snackBarMsg.postValue(e.getMessage(context))
            } catch (e: Exception) {
                _snackBarMsg.postValue(context.getString(R.string.error_join_flat_failed))
            }
        }
    }

}