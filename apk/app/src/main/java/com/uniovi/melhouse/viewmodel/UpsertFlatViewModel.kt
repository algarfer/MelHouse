package com.uniovi.melhouse.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.Executor
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.repository.flat.FlatRepositorySupabase
import com.uniovi.melhouse.factories.viewmodel.UpsertFlatViewModelFactory
import com.uniovi.melhouse.utils.ifEmptyNull
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = UpsertFlatViewModelFactory::class
)
class UpsertFlatViewModel @AssistedInject constructor(
    private val flatRepository: FlatRepositorySupabase,
    private val userSessionFacade: SupabaseUserSessionFacade,
    @Assisted private val flat: Flat?
) : ViewModel() {

    var name: String? = null
    var address: String? = null
    var floor: Int? = null
    var door: String? = null
    var stair: String? = null

    val nameError: LiveData<String?>
        get() = _nameError
    private val _nameError: MutableLiveData<String?> = MutableLiveData(null)
    val addressError: LiveData<String?>
        get() = _addressError
    private val _addressError: MutableLiveData<String?> = MutableLiveData(null)
    val creationSuccessful: LiveData<Boolean>
        get() = _creationSuccessful
    private val _creationSuccessful = MutableLiveData(false)

    fun upsertFlat(context: Context) {
        var areErrors = false
        if(name.isNullOrEmpty()) {
            _nameError.postValue(context.getString(R.string.error_form_flat_name_empty))
            areErrors = true
        }
        if(address.isNullOrEmpty()) {
            _addressError.postValue(context.getString(R.string.error_form_flat_address_empty))
            areErrors = true
        }

        if(areErrors) return

        if(flat == null)
            upsert { flatRepository.createFlat(generateFlat()) }
        else
            upsert { flatRepository.update(generateFlat()) }
    }

    private fun upsert(action: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            Executor.safeCall {
                action()
                _creationSuccessful.postValue(true)
            }
        }
    }

    private fun generateFlat(): Flat {
        return flat?.copy(
            name = name!!,
            address = address!!,
            floor = floor,
            door = door?.ifEmptyNull(),
            stair = stair?.ifEmptyNull()
        ) ?: Flat(
            name = name!!,
            address = address!!,
            floor = floor,
            door = door?.ifEmptyNull(),
            stair = stair?.ifEmptyNull(),
            adminId = userSessionFacade.getUserId()!!
        )
    }
}