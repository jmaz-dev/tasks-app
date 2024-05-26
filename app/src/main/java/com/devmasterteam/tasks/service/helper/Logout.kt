package com.devmasterteam.tasks.service.helper

import android.content.Context
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.SecurityPreferences

class Logout {

    companion object {
        fun logout(context: Context) {
            val security = SecurityPreferences(context)
            security.remove(TaskConstants.SHARED.TOKEN_KEY)
            security.remove(TaskConstants.SHARED.PERSON_KEY)
            security.remove(TaskConstants.SHARED.PERSON_NAME)
            security.remove(TaskConstants.SHARED.PERSON_EMAIL)
            security.remove(TaskConstants.SHARED.BIOMETRIC)
        }
    }
}