package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val security = SecurityPreferences(application.applicationContext)

    fun logout() {
        security.remove(TaskConstants.SHARED.TOKEN_KEY)
        security.remove(TaskConstants.SHARED.PERSON_KEY)
        security.remove(TaskConstants.SHARED.PERSON_NAME)
    }
}