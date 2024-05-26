package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.helper.Logout
import com.devmasterteam.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val security = SecurityPreferences(application.applicationContext)

    private val mPersonName = MutableLiveData<String>()
    var personName: LiveData<String> = mPersonName


    fun logout() {
        Logout.logout(getApplication())
    }

    fun loadUserName() {
        mPersonName.value = security.get(TaskConstants.SHARED.PERSON_NAME)
    }

    fun toggleBiometric(value: Boolean) {
        security.store(TaskConstants.SHARED.BIOMETRIC, value.toString())
    }

    fun isBiometricActive(): Boolean {
        return security.get(TaskConstants.SHARED.BIOMETRIC) == "true"
    }

}