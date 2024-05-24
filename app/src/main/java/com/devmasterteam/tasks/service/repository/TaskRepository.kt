package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context) : BaseRepository(context) {

    var service = RetrofitClient.getService(TaskService::class.java)

    fun getAll(listener: APIListener<List<TaskModel>>) {
        val call = service.getTasks()
        executeCall(call, listener)
    }

    fun getById(id: Int, listener: APIListener<TaskModel>) {
        val call = service.getTaskById(id)
        executeCall(call, listener)

    }

    fun getNext7Days(listener: APIListener<List<TaskModel>>) {
        val call = service.getNextWeekTasks()
        executeCall(call, listener)

    }

    fun getOverdue(listener: APIListener<List<TaskModel>>) {
        val call = service.getOverdue()
        executeCall(call, listener)

    }

    fun createTask(task: TaskModel, listener: APIListener<Boolean>) {

        val call = service.newTask(
            priorityId = task.priorityId,
            description = task.description,
            dueDate = task.dueDate,
            complete = task.complete
        )
        executeCall(call, listener)

    }

    fun updateTask(task: TaskModel, listener: APIListener<Boolean>) {
        val call = service.updateTask(
            id = task.id,
            priorityId = task.priorityId,
            description = task.description,
            dueDate = task.dueDate,
            complete = task.complete
        )
        executeCall(call, listener)

    }

    fun completeTask(id: Int, listener: APIListener<Boolean>) {
        val call = service.setCompleteTask(id)
        executeCall(call, listener)
    }

    fun undoTask(id: Int, listener: APIListener<Boolean>) {
        val call = service.setUndoTask(id)
        executeCall(call, listener)
    }

    fun deleteTask(id: Int, listener: APIListener<Boolean>) {
        val call = service.deleteTask(id)
        executeCall(call, listener)

    }

}