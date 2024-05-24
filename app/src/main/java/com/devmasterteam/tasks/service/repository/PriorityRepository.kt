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

class PriorityRepository(val context: Context) : BaseRepository() {

    private var service = RetrofitClient.getService(PriorityService::class.java)
    private val taskDatabase = TaskDatabase.getDatabase(context).priorityDAO()


    /*Remote CAll and Local Database inside response*/
    fun listFromApi(listener: APIListener<List<PriorityModel>>) {
        val call = service.getPriority()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }


        })
    }

    fun listFromDatabase(): List<PriorityModel> {
        return taskDatabase.getPriorities()
    }

    fun dataBaseSave(list: List<PriorityModel>) {
        taskDatabase.clear()
        taskDatabase.insert(list)
    }
}