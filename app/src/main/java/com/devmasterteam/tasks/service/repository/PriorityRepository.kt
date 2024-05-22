package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) {

    private var service = RetrofitClient.getService(PriorityService::class.java)
    private val taskDatabase = TaskDatabase.getDatabase(context).priorityDAO()

    val codeSuccess = TaskConstants.HTTP.SUCCESS

    private fun failResponde(message: String): String {
        return Gson().fromJson(message, String::class.java)
    }

    /*Remote CAll and Local Database inside response*/
    fun listFromApi(listener: APIListener<Boolean>) {
        val call = service.getPriority()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                if (response.code() == codeSuccess) {
                    response.body()?.let {
                        taskDatabase.clear()
                        taskDatabase.insert(it)
                        listener.onSuccess(true)
                    }
                } else listener.onFailure(failResponde(response.errorBody()!!.string()))


            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }


        })
    }

    fun listFromDatabase(): List<PriorityModel> {
        return taskDatabase.getPriorities()
    }

//    fun setPriorities() {
//        val priorities = listFromDatabase()
//        taskDatabase.insert(priorities)
//    }
}