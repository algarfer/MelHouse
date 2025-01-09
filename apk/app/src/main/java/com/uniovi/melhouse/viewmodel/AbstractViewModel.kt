package com.uniovi.melhouse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class AbstractViewModel : ViewModel() {
    val genericError: LiveData<String?>
        get() = _genericError
    protected val _genericError = MutableLiveData<String?>(null)

    fun clearGenericError() {
        _genericError.value = null
    }

    open fun clearAllErrors() {
        _genericError.value = null
    }
}