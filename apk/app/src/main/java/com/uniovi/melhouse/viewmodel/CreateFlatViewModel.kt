package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.repository.flat.FlatRepositorySupabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFlatViewModel @Inject constructor(
    private val flatRepository: FlatRepositorySupabase,
    private val userSessionFacade: SupabaseUserSessionFacade
) : ViewModel() {

    private val _nameError: MutableLiveData<String?> = MutableLiveData(null)
    val nameError: LiveData<String?>
        get() = _nameError
    private val _addressError: MutableLiveData<String?> = MutableLiveData(null)
    val addressError: LiveData<String?>
        get() = _addressError
    val creationSuccessful: LiveData<Boolean>
        get() = _creationSuccessful
    private val _creationSuccessful = MutableLiveData(false)

    fun createFlat(name: String, address: String, floor: String, door: String, stair: String, rooms: String, context: Context) {
        var areErrors = false
        if(name.isEmpty()) {
            _nameError.postValue(context.getString(R.string.error_form_flat_name_empty))
            areErrors = true
        }
        if(address.isEmpty()) {
            _addressError.postValue(context.getString(R.string.error_form_flat_address_empty))
            areErrors = true
        }

        if(areErrors) return

        viewModelScope.launch(Dispatchers.IO) {
            flatRepository.createFlat(
                Flat(
                    name = name,
                    address = address,
                    floor = floor.toIntOrNull(),
                    door = door,
                    stair = stair,
                    adminId = userSessionFacade.getUserId()!!
                )
            )
            _creationSuccessful.postValue(true)
        }
    }
}