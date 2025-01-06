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
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.exceptions.PersistenceLayerException
import com.uniovi.melhouse.factories.viewmodel.UpsertFlatViewModelFactory
import com.uniovi.melhouse.preference.Prefs
import com.uniovi.melhouse.utils.ifEmptyNull
import com.uniovi.melhouse.utils.validateLength
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = UpsertFlatViewModelFactory::class
)
class UpsertFlatViewModel @AssistedInject constructor(
    private val flatRepository: FlatRepository,
    private val userSessionFacade: SupabaseUserSessionFacade,
    private val prefs: Prefs,
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
    val genericError: LiveData<String?>
        get() = _genericError
    private val _genericError = MutableLiveData<String?>(null)

    fun upsertFlat(context: Context) {
        var areErrors = false
        val name = name
        if(name.isNullOrEmpty()) {
            _nameError.postValue(context.getString(R.string.error_form_flat_name_empty))
            areErrors = true
        } else if(!name.validateLength()) {
            _nameError.postValue(context.getString(R.string.error_form_flat_name_length))
            areErrors = true
        }
        if(address.isNullOrEmpty()) {
            _addressError.postValue(context.getString(R.string.error_form_flat_address_empty))
            areErrors = true
        }

        if(areErrors) return

        if(flat == null)
            upsert {
                val flat = flatRepository.createFlat(generateFlat())
                prefs.setFlatId(flat.id)
            }
        else
            upsert { flatRepository.update(generateFlat()) }
    }

    private fun upsert(action: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Executor.safeCall {
                    action()
                    _creationSuccessful.postValue(true)
                }
            } catch (e: PersistenceLayerException) {
                _genericError.postValue(e.message)
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