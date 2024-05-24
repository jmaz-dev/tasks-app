package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Response

open class BaseRepository {
    val codeSuccess = TaskConstants.HTTP.SUCCESS

    fun failResponse(message: String): String {
        return Gson().fromJson(message, String::class.java)
    }

    fun <T> handleResponse(response: Response<T>, listener: APIListener<T>) {
        if (response.code() == codeSuccess) {
            response.body()?.let {
                listener.onSuccess(it)
            }
        } else listener.onFailure(failResponse(response.errorBody()!!.string()))
    }
}