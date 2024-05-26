package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PersonRepository(application.applicationContext)
    private val shared = SecurityPreferences(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _create = MutableLiveData<ValidationModel>()
    val create: LiveData<ValidationModel> = _create

    fun createAccount(name: String, email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _create.value = ValidationModel(
                this.getApplication<Application>().getString(R.string.as_senhas_n_o_conferem)
            )
        } else {
            repository.create(name, email, password, object : APIListener<PersonModel> {
                override fun onSuccess(result: PersonModel) {

                    getPriorities()

                    shared.store(TaskConstants.SHARED.PERSON_NAME, result.name)
                    shared.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                    shared.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)

                    RetrofitClient.addHeaders(result.token, result.personKey)

                    _create.value = ValidationModel(status = true)
                }

                override fun onFailure(result: String) {
                    _create.value = ValidationModel(result)
                }
            })

        }
    }

    private fun getPriorities() {
        priorityRepository.listFromApi(object : APIListener<List<PriorityModel>> {
            override fun onSuccess(result: List<PriorityModel>) {
                priorityRepository.dataBaseSave(result)
            }

            override fun onFailure(result: String) {
                ValidationModel(result)
            }
        })

    }

}