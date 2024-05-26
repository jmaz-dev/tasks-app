package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.helper.BiometricHelper
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _islogged = MutableLiveData<ValidationModel>()
    val islogged: LiveData<ValidationModel> = _islogged


    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel> {

            override fun onSuccess(result: PersonModel) {

                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)
                securityPreferences.store(TaskConstants.SHARED.PERSON_EMAIL, email)

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
        val email = securityPreferences.get(TaskConstants.SHARED.PERSON_EMAIL)
        val biometric = securityPreferences.get(TaskConstants.SHARED.BIOMETRIC)

        RetrofitClient.addHeaders(token, personKey)

        val logged = (token != "" && personKey != "")

        if (logged) {
            if (biometric.toBoolean() && BiometricHelper.isBiometricAvailable(getApplication())) {
                _islogged.value = ValidationModel(email, true)
            } else {
                _islogged.value = ValidationModel(email, false)
            }
        }
    }

    fun getPriorities() {
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