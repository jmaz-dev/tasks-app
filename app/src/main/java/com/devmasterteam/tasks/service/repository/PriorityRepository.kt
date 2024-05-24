package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) : BaseRepository(context) {

    private var service = RetrofitClient.getService(PriorityService::class.java)
    private val taskDatabase = TaskDatabase.getDatabase(context).priorityDAO()

    companion object {
        private var priorityCached: List<PriorityModel>? = null
    }

    /*Remote CAll and Local Database inside response*/
    fun listFromApi(listener: APIListener<List<PriorityModel>>) {
        val call = service.getPriority()
        executeCall(call, listener)

    }

    fun listFromDatabase(): List<PriorityModel> {
        if (priorityCached == null) {
            priorityCached = taskDatabase.getPriorities()
        }
        return priorityCached as List<PriorityModel>
    }

    fun dataBaseSave(list: List<PriorityModel>) {
        taskDatabase.clear()
        taskDatabase.insert(list)
    }
}