package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application)
    private val securityPreferences = SecurityPreferences(application.applicationContext)
    private val priorityRepository = PriorityRepository(application)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _islogged = MutableLiveData<Boolean>()
    val islogged: LiveData<Boolean> = _islogged

    private val _priorities = MutableLiveData<ValidationModel>()
    val priorities: LiveData<ValidationModel> = _priorities


    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel> {

            override fun onSuccess(result: PersonModel) {

                getPriorities()

                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                RetrofitClient.addHeaders(result.token, result.personKey)

                _login.value = ValidationModel(status = true)
            }

            override fun onFailure(result: String) {
                _login.value = ValidationModel(result)
            }

        })
    }


    fun verifyLoggedUser() {
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeaders(token, personKey)

        if (token != "" && personKey != "") {
            _islogged.value = true
        } else {
            getPriorities()
        }
    }

    private fun getPriorities() {
        priorityRepository.listFromApi(object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                _priorities.value = ValidationModel(status = true)
            }

            override fun onFailure(result: String) {
                _priorities.value = ValidationModel(result)
            }

        })

    }

}